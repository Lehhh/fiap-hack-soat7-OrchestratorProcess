package br.com.fiap.soat7.infrastructure.config.props.k8s;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class K8s {
	private List<Pod> pods;
	private String namespace;
}
