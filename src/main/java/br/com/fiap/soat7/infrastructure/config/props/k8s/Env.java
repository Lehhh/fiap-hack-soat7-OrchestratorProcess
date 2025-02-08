package br.com.fiap.soat7.infrastructure.config.props.k8s;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Env {
	private String key;
	private String value;
}
