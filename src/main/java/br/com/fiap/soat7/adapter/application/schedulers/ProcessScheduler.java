package br.com.fiap.soat7.adapter.application.schedulers;

import br.com.fiap.soat7.adapter.application.usecase.WorkflowUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessScheduler {

	private final WorkflowUseCase workflowUseCase;


	@Scheduled(fixedDelay = 3000)
	public void executeUploadVideoS3() throws Exception {
		workflowUseCase.executeUploadVideoS3();
	}

	@Scheduled(fixedDelay = 3000)
	public void executeProcessVideo() throws Exception {
		workflowUseCase.executeProcessVideo();
	}

	@Scheduled(fixedDelay = 3000)
	public void executeUploadImageS3() throws Exception {
		workflowUseCase.executeUploadImageS3();
	}
}
