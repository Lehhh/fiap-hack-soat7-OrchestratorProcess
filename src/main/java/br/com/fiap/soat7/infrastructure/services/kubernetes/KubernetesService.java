package br.com.fiap.soat7.infrastructure.services.kubernetes;

import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import br.com.fiap.soat7.infrastructure.config.props.k8s.Pod;
import br.com.fiap.soat7.domain.helper.GenerateRandomString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.Config;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class KubernetesService {

	private final CoreV1Api api;
	private final GenerateRandomString randomString;
	private final OrchestratorProcessProperties props;

	public KubernetesService(GenerateRandomString randomString, OrchestratorProcessProperties props) throws Exception {
		ApiClient client = Config.defaultClient();
		Configuration.setDefaultApiClient(client);
		this.api = new CoreV1Api();
		this.randomString = randomString;
		this.props = props;
	}

	public void createEphemeralPod(Pod pod) throws Exception {
		String podName = pod.getName() + randomString.generateRandomString().toLowerCase();
		try {
			List<V1EnvVar> envs = pod.getEnvs().stream().map(e -> {
				V1EnvVar v1EnvVar = new V1EnvVar();
				v1EnvVar.setName(e.getKey());
				v1EnvVar.setValue(e.getValue());
				return v1EnvVar;
			}).toList();
			log.info("Criando pod na namespace {}", props.getK8s().getNamespace());
			V1Pod podv1 = new V1Pod()
					.apiVersion("v1")
					.kind("Pod")
					.metadata(new V1ObjectMeta().name(podName).namespace(props.getK8s().getNamespace()))
					.spec(new V1PodSpec()
							.serviceAccount("my-app-sa")
							.restartPolicy("Never")
							.addContainersItem(new V1Container()
									.name(podName)
									.image(pod.getImage())
									.env(envs)
									.volumeMounts(List.of(
											new V1VolumeMount()
													.name("app-storage")
													.mountPath("/opt/app/shared")
									)))
							.volumes(List.of(
									new V1Volume()
											.name("app-storage")
											.persistentVolumeClaim(
													new V1PersistentVolumeClaimVolumeSource()
															.claimName("app-pvc")
											)
							)));
			api.createNamespacedPod( props.getK8s().getNamespace(), podv1, null, null, null, null);
		}
		catch (ApiException e) {
			log.error("Erro ao criar Pod: " + e.getMessage());
			log.error("Código de resposta: " + e.getCode());
			log.error("Resposta da API: " + e.getResponseBody());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				api.deleteNamespacedPod(podName, props.getK8s().getNamespace(), null, null, null, null, null, null);
				log.info("Pod {} deletado devido a erro", podName);
			} catch (ApiException e) {
				log.error("Erro ao deletar Pod: " + e.getMessage());
				log.error("Código de resposta: " + e.getCode());
				log.error("Resposta da API: " + e.getResponseBody());
				e.printStackTrace();
			}
		}
	}

	public List<String> listarPodsComPrefixo(String prefixo) throws ApiException {
		log.info("Iniciando listagem dos pods com prefixo: {}", prefixo);
		V1PodList podList = api.listNamespacedPod(props.getK8s().getNamespace(), null, null, null, null, null, null, null, null, null, false);
		return podList.getItems().stream()
				.map(pod -> pod.getMetadata().getName())
				.filter(nome -> nome.startsWith(prefixo))
				.toList();
	}

}
