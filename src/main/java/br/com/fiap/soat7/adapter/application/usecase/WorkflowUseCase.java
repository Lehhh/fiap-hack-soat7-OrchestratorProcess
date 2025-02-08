package br.com.fiap.soat7.adapter.application.usecase;

import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import br.com.fiap.soat7.infrastructure.config.props.k8s.Pod;
import br.com.fiap.soat7.infrastructure.services.kubernetes.KubernetesService;
import br.com.fiap.soat7.infrastructure.services.redis.RedisService;
import io.kubernetes.client.openapi.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
		int podsRequired = (int) Math.ceil((double) videosToUpload / maxExceutionPerPod);
		int size = kubernetesService.listarPodsComPrefixo(uploadVideo).size();
		for (int i = 0; i < podsRequired - size; i++) {
			Pod pod = props.getK8s().getPods().stream().filter(p -> p.getName().equals(uploadVideo)).findFirst().get();
			kubernetesService.createEphemeralPod(pod);
		}
	}

	public void executeProcessVideo() throws Exception {
		int videosToProcess = redisService.fetchQueueVideoProcess().size();
		int podsRequired = (int) Math.ceil((double) videosToProcess / maxExceutionPerPod);
		int size = kubernetesService.listarPodsComPrefixo(processVideo).size();
		for (int i = 0; i < podsRequired - size; i++) {
			Pod pod = props.getK8s().getPods().stream().filter(p -> p.getName().equals(processVideo)).findFirst().get();
			kubernetesService.createEphemeralPod(pod);
		}
	}
	public void executeUploadImageS3() throws Exception {
		int imagesToUpload = redisService.fetchQueueImageS3().size();
		int podsRequired = (int) Math.ceil((double) imagesToUpload / maxExceutionPerPod);
		int size = kubernetesService.listarPodsComPrefixo(uploadImage).size();
		for (int i = 0; i < podsRequired - size; i++) {
			Pod pod = props.getK8s().getPods().stream().filter(p -> p.getName().equals(imagesToUpload)).findFirst().get();
			kubernetesService.createEphemeralPod(pod);
		}
	}






}
