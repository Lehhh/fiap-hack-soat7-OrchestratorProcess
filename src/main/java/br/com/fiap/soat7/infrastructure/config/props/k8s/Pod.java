package br.com.fiap.soat7.infrastructure.config.props.k8s;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Pod {
	private String name;
	private String image;
	private List<Env> envs;
}
