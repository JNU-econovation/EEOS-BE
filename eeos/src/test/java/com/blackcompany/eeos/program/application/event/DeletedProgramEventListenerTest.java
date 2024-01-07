package com.blackcompany.eeos.program.application.event;

import static org.mockito.Mockito.verify;

import com.blackcompany.eeos.attend.application.service.CommandAttendService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith({SpringExtension.class, MockitoExtension.class})
class DeletedProgramEventListenerTest {
	@Mock private CommandAttendService attendService;

	@Mock private ApplicationEventPublisher eventPublisher;

	@Mock private DeletedProgramEventListener eventListener;

	@Test
	void handleDeletedProgram_shouldDeleteAttendees()
			throws ExecutionException, InterruptedException {
		// Given
		DeletedProgramEvent event = DeletedProgramEvent.of(1L);

		// When
		CompletableFuture<Void> future =
				CompletableFuture.runAsync(
						() -> {
							try {
								eventListener.handleDeletedProgram(event);
							} catch (Exception e) {
								// Handle exceptions if needed
							}
						});

		// Then
		future.get(); // This will block and wait for the asynchronous method to complete
		verify(attendService).deleteByProgram(1L);
	}

	@Test
	void handleDeletedProgram_shouldPublishEvent() {
		// Given
		DeletedProgramEvent event = DeletedProgramEvent.of(1L);

		// When
		eventPublisher.publishEvent(event);

		// Then
		verify(eventListener).handleDeletedProgram(event);
	}
}
