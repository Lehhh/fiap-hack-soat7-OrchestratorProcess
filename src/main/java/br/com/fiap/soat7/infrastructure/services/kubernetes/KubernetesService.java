package br.com.fiap.soat7.infrastructure.services.kubernetes;

import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import br.com.fiap.soat7.infrastructure.config.props.k8s.Pod;
import br.com.fiap.soat7.domain.helper.GenerateRandomString;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
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

	public void scaleDeployment(String deploymentName, int replicas) throws Exception {
		try {
			AppsV1Api appsApi = new AppsV1Api();
			V1Deployment deployment = appsApi.readNamespacedDeployment(deploymentName, props.getK8s().getNamespace(), null);
			deployment.getSpec().setReplicas(replicas);
			appsApi.replaceNamespacedDeployment(deploymentName, props.getK8s().getNamespace(), deployment, null, null, null, null);
			log.info("Deployment {} scaled to {} replicas in namespace {}", deploymentName, replicas, props.getK8s().getNamespace());
		} catch (ApiException e) {
			log.error("Error scaling Deployment: " + e.getMessage());
			log.error("Response code: " + e.getCode());
			log.error("API response: " + e.getResponseBody());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	public int listarQuantidadeReplicas(String deploymentName) throws ApiException {
		log.info("Iniciando listagem da quantidade de replicas para o deployment: {}", deploymentName);
		AppsV1Api appsApi = new AppsV1Api();
		V1Deployment deployment = appsApi.readNamespacedDeployment(deploymentName, props.getK8s().getNamespace(), null);
		return deployment.getSpec().getReplicas();
	}

}
