package com.blackcompany.eeos.program.application.event;

import com.blackcompany.eeos.calendar.application.dto.CalendarCreateCommand;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.calendar.application.usecase.CreateCalendarUsecase;
import com.blackcompany.eeos.program.application.exception.NotFoundProgramException;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CreatedProgramEventListener {

	private final CreateCalendarUsecase calendarUsecase;
	private final ProgramRepository programRepository;
	private final ProgramEntityConverter entityConverter;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(
			value = CreatedProgramEvent.class,
			phase = TransactionPhase.AFTER_COMMIT)
	public void createCalendar(CreatedProgramEvent event) {
		Long programId = event.getProgramId();

		ProgramModel program =
				entityConverter.from(
						programRepository
								.findById(programId)
								.orElseThrow(() -> new NotFoundProgramException(programId)));

		Long programDateMilli = program.getProgramDate().toInstant().toEpochMilli();

		CalendarCreateCommand command =
				new CalendarCreateCommand(
						program.getTitle(),
						null,
						CalendarType.PRESENTATION.name().toLowerCase(Locale.ROOT),
						programDateMilli,
						programDateMilli);

		calendarUsecase.create(command);
	}
}
