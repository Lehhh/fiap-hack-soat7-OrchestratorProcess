package br.com.fiap.soat7.infrastructure.services.redis;

import br.com.fiap.soat7.domain.enums.StatusRequest;
import br.com.fiap.soat7.infrastructure.config.props.OrchestratorProcessProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisService {

	private final RestTemplate restTemplate;
	private final OrchestratorProcessProperties props;

	public List<String> fetchQueueVideoProcess(){
		ResponseEntity<List<String>> exchange = restTemplate.exchange(
				props.getRedisMidUrl() + StatusRequest.PROCESS_VIDEO_QUEUE.getEndPoint(),
				HttpMethod.GET,
				new HttpEntity<>(null),
				new ParameterizedTypeReference<List<String>>() {}
		);
		return exchange.getBody();
	}

	public List<String> fetchQueueVideoS3(){
		ResponseEntity<List<String>> exchange = restTemplate.exchange(props.getRedisMidUrl() + StatusRequest.UPLOAD_S3_QUEUE.getEndPoint(),
				HttpMethod.GET,
				new HttpEntity<>(null),
				new ParameterizedTypeReference<List<String>>() {}
		);
		return exchange.getBody();
	}

	public List<String> fetchQueueImageS3(){
		ResponseEntity<List<String>> exchange = restTemplate.exchange(props.getRedisMidUrl() + StatusRequest.UPLOAD_S3_IMAGES_QUEUE.getEndPoint(),
				HttpMethod.GET,
				new HttpEntity<>(null),
				new ParameterizedTypeReference<List<String>>() {}
		);
		return exchange.getBody();
	}

}