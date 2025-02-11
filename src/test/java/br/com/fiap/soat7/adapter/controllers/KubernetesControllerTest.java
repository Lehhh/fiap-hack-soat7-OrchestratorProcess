package br.com.fiap.soat7.adapter.controllers;

import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import br.com.fiap.soat7.infrastructure.services.kubernetes.KubernetesService;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KubernetesServiceTest {

    @Mock
    private CoreV1Api coreV1Api;

    @Mock
    private AppsV1Api appsV1Api;

    @Mock
    private OrchestratorProcessProperties props;

    @InjectMocks
    private KubernetesService kubernetesService;

    @BeforeEach
    public void setUp() throws Exception {
        when(props.getK8s().getNamespace()).thenReturn("default");
    }

//    @Test
//    public void testScaleDeployment() throws Exception {
//        V1Deployment deployment = new V1Deployment();
//        V1DeploymentSpec spec = new V1DeploymentSpec();
//        deployment.setSpec(spec);
//
//        when(appsV1Api.readNamespacedDeployment(anyString(), anyString(), anyString())).thenReturn(deployment);
//
//        kubernetesService.scaleDeployment("test-deployment", 3);
//
//        assertEquals(3, deployment.getSpec().getReplicas());
//        verify(appsV1Api, times(1)).replaceNamespacedDeployment(anyString(), anyString(), eq(deployment), any(), any(), any(), any());
//    }
//
//    @Test
//    public void testListarQuantidadeReplicas() throws ApiException {
//        V1Deployment deployment = new V1Deployment();
//        V1DeploymentSpec spec = new V1DeploymentSpec();
//        spec.setReplicas(5);
//        deployment.setSpec(spec);
//
//        when(appsV1Api.readNamespacedDeployment(anyString(), anyString(), anyString())).thenReturn(deployment);
//
//        int replicas = kubernetesService.listarQuantidadeReplicas("test-deployment");
//
//        assertEquals(5, replicas);
//    }
}