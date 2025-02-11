package br.com.fiap.soat7.adapter.application.usecase;

import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import br.com.fiap.soat7.infrastructure.config.props.k8s.Pod;
import br.com.fiap.soat7.infrastructure.services.kubernetes.KubernetesService;
import br.com.fiap.soat7.infrastructure.services.redis.RedisService;
import io.kubernetes.client.openapi.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class WorkflowUseCase {

	private final KubernetesService kubernetesService;
	private final OrchestratorProcessProperties props;
	private final RedisService redisService;

	@Value("${UPLOAD_VIDEO}")
	private String uploadVideo;

	@Value("${PROCESS_VIDEO}")
	private String processVideo;

	@Value("${UPLOAD_IMAGE}")
	private String uploadImage;

	@Value("${MAX_EXECUTION_PER_POD}")
	private Integer maxExceutionPerPod;

	public void executeUploadVideoS3() throws Exception {
		int videosToUpload = redisService.fetchQueueVideoS3().size();
		extracted(videosToUpload, uploadVideo);
	}
	public void executeProcessVideo() throws Exception {
		int videosToProcess = redisService.fetchQueueVideoProcess().size();
		extracted(videosToProcess, processVideo);
	}

	public void executeUploadImageS3() throws Exception {
		int imagesToUpload = redisService.fetchQueueImageS3().size();
		extracted(imagesToUpload, uploadImage);
	}

	private void extracted(int videosToProcess, String deploymentName) throws Exception {

		int podsRequired = (videosToProcess + maxExceutionPerPod - 1) / maxExceutionPerPod;
		int size = kubernetesService.listarQuantidadeReplicas(deploymentName);
		size = size == 0 ? 1 : size;
        if (size < podsRequired) {
            kubernetesService.scaleDeployment(deploymentName, podsRequired);
        } else {
            log.info("Deployment {} já possui {} replicas", deploymentName, size);
        }
    }








}
