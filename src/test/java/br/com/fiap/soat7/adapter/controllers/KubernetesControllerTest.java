package br.com.fiap.soat7.adapter.controllers;

import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import br.com.fiap.soat7.infrastructure.config.props.k8s.K8s;
import br.com.fiap.soat7.infrastructure.config.props.k8s.Pod;
import br.com.fiap.soat7.infrastructure.services.kubernetes.KubernetesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KubernetesControllerTest {

    @InjectMocks
    private KubernetesController kubernetesController;

    @Mock
    private KubernetesService kubernetesService;

    @Mock
    private OrchestratorProcessProperties props;

    private Pod pod;
    private K8s k8s;

    @BeforeEach
    void setUp() {
        pod = new Pod();
        pod.setName("test-pod");
        k8s = new K8s();
        List<Pod> podList = Collections.singletonList(pod);
        k8s.setPods(podList);
    }

    @Test
    void createPod_shouldReturnOkAndCreatePodWhenPodExists() throws Exception {
        // Arrange
        String podName = "test-pod";
        when(props.getK8s()).thenReturn(k8s);


        // Act
        ResponseEntity<String> response = kubernetesController.createPod(podName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pod " + podName + " criado com sucesso!", response.getBody());
        verify(kubernetesService, times(1)).createEphemeralPod(pod);
    }

    @Test
    void createPod_shouldReturnInternalServerErrorWhenPodDoesNotExist() {
        // Arrange
        String podName = "nonexistent-pod";
        when(props.getK8s()).thenReturn(k8s);


        // Act
        ResponseEntity<String> response = kubernetesController.createPod(podName);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao criar pod", response.getBody());
    }

    @Test
    void createPod_shouldReturnInternalServerErrorWhenKubernetesServiceThrowsException() throws Exception {
        // Arrange
        String podName = "test-pod";
        when(props.getK8s()).thenReturn(k8s);
        doThrow(new RuntimeException("Simulated Kubernetes Exception")).when(kubernetesService).createEphemeralPod(pod);

        // Act
        ResponseEntity<String> response = kubernetesController.createPod(podName);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao criar pod: Simulated Kubernetes Exception", response.getBody());
        verify(kubernetesService, times(1)).createEphemeralPod(pod);
    }

    @Test
    void createPod_shouldReturnInternalServerErrorWhenPropsThrowsException() throws Exception {
        // Arrange
        String podName = "test-pod";
        when(props.getK8s()).thenThrow(new RuntimeException("Simulated Props Exception"));

        // Act
        ResponseEntity<String> response = kubernetesController.createPod(podName);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao criar pod: Simulated Props Exception", response.getBody());
        verify(kubernetesService, never()).createEphemeralPod(any());
    }

}