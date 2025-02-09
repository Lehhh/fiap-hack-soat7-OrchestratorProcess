package br.com.fiap.soat7.adapter.application.schedulers;

import br.com.fiap.soat7.adapter.application.usecase.WorkflowUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class ProcessScheduler {

	private final WorkflowUseCase workflowUseCase;

	@Scheduled(fixedDelay = 30000)
	public void executeUploadVideoS3() throws Exception {
		log.info("Iniciando workflow de upload de video para o S3");
		workflowUseCase.executeUploadVideoS3();
	}

	@Scheduled(fixedDelay = 30000)
	public void executeProcessVideo() throws Exception {
		log.info("Iniciando workflow de processamento de video para o S3");
		workflowUseCase.executeProcessVideo();
	}

	@Scheduled(fixedDelay = 30000)
	public void executeUploadImageS3() throws Exception {
		log.info("Iniciando workflow de upload de imagens para o S3");
		workflowUseCase.executeUploadImageS3();
	}
}
