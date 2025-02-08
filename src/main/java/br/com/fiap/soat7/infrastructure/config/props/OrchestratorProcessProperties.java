package br.com.fiap.soat7.infrastructure.config.props;

import br.com.fiap.soat7.infrastructure.config.props.k8s.K8s;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("br.com.fiap.soat7")
@Getter
@Setter
public class OrchestratorProcessProperties {
	private String redisMidUrl;
	private String maxExecutionPerPod;
	private K8s k8s;
}
