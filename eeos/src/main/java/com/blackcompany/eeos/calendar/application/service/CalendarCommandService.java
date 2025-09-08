package com.blackcompany.eeos.calendar.application.service;

import com.blackcompany.eeos.calendar.application.dto.CalendarCreateCommand;
import com.blackcompany.eeos.calendar.application.dto.CalendarUpdateCommand;
import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.repository.CalendarRepository;
import com.blackcompany.eeos.calendar.application.usecase.CreateCalendarUsecase;
import com.blackcompany.eeos.calendar.application.usecase.DeleteCalendarUsecase;
import com.blackcompany.eeos.calendar.application.usecase.UpdateCalendarUsecase;
import com.blackcompany.eeos.calendar.application.validator.CalendarValidator;
import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.common.utils.RequestScope;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarCommandService implements CreateCalendarUsecase, UpdateCalendarUsecase, DeleteCalendarUsecase {

	private final MemberRepository memberRepository;
	private final CalendarRepository repository;
	private final CalendarValidator validator;

	@Override
	@Transactional
	public Long create(CalendarCreateCommand command) {
		Long memberId = RequestScope.getMemberId();
		CalendarModel calendar = newCalendar(command, memberId);
		MemberModel member = memberRepository.findById(memberId);

		validator.typeValidate(calendar, member.getDepartment());

		return repository.save(calendar);
	}

	@Override
	public Long update(Long calendarId, CalendarUpdateCommand command) {
		Long memberId = RequestScope.getMemberId();

		CalendarModel model = repository.findById(calendarId);

		updateCalendar(memberId, command, model);

		repository.save(model);

		return model.getId();
	}

	@Override
	public void delete(Long calendarId) {
		Long memberId = RequestScope.getMemberId();

		CalendarModel model = repository.findById(calendarId);

		model.validateUpdate(memberId);

		repository.delete(calendarId);
	}

	private void updateCalendar(Long memberId, CalendarUpdateCommand command, CalendarModel model) {
		model.validateUpdate(memberId);
		LocalDateTime startAt = DateConverter.toLocalDateTime(command.startAt());
		LocalDateTime endAt = DateConverter.toLocalDateTime(command.endAt());

		model
				.updateEndAt(endAt)
				.updateStartAt(startAt)
				.updateTitle(command.title())
				.updateUrl(command.url())
				.updateType(command.type());
	}

	private CalendarModel newCalendar(CalendarCreateCommand command, Long memberId) {
		LocalDateTime startAt = DateConverter.toLocalDateTime(command.startAt());
		LocalDateTime endAt = DateConverter.toLocalDateTime(command.endAt());
		return CalendarModel.create(
				command.title(), startAt, endAt, command.type(), command.url(), memberId);
	}
}
