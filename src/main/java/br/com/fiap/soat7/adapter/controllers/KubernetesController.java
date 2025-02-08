package br.com.fiap.soat7.adapter.controllers;

import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import br.com.fiap.soat7.infrastructure.config.props.k8s.Pod;
import br.com.fiap.soat7.infrastructure.services.kubernetes.KubernetesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/k8s")
@RequiredArgsConstructor
public class KubernetesController {

	private final KubernetesService kubernetesService;
	private final OrchestratorProcessProperties props;


	@PostMapping("/create-pod")
	public ResponseEntity<String>  createPod(@RequestParam String podName) {
		try {
			Optional<Pod> pod = props.getK8s().getPods().stream().filter(p -> p.getName().equals(podName)).findFirst();
			return pod.map(pod1 -> {
						try {
							kubernetesService.createEphemeralPod(pod1);
							return ResponseEntity.ok().body("Pod " + podName + " criado com sucesso!");
						} catch (Exception e) {
							return ResponseEntity.internalServerError().body("Erro ao criar pod: " + e.getMessage());
						}
					}
				).orElse(ResponseEntity.internalServerError().body("Erro ao criar pod"));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Erro ao criar pod: " + e.getMessage());
		}
	}
}
