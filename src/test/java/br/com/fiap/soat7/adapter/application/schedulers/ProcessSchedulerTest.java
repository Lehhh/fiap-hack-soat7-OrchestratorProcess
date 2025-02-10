package br.com.fiap.soat7.adapter.application.schedulers;

import br.com.fiap.soat7.adapter.application.usecase.WorkflowUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessSchedulerTest {

    @InjectMocks
    private ProcessScheduler processScheduler;

    @Mock
    private WorkflowUseCase workflowUseCase;

    private static final Logger log = LoggerFactory.getLogger(ProcessSchedulerTest.class);


    @Test
    void executeUploadVideoS3_shouldExecuteWorkflow() throws Exception {
        // Arrange (Nothing to arrange in this case)

        // Act
        processScheduler.executeUploadVideoS3();

        // Assert
        verify(workflowUseCase, times(1)).executeUploadVideoS3();
    }

    @Test
    void executeProcessVideo_shouldExecuteWorkflow() throws Exception {
        // Arrange

        // Act
        processScheduler.executeProcessVideo();

        // Assert
        verify(workflowUseCase, times(1)).executeProcessVideo();
    }

    @Test
    void executeUploadImageS3_shouldExecuteWorkflow() throws Exception {
        // Arrange

        // Act
        processScheduler.executeUploadImageS3();

        // Assert
        verify(workflowUseCase, times(1)).executeUploadImageS3();
    }

}