package com.blackcompany.eeos.calendar.application.service;

import com.blackcompany.eeos.calendar.application.dto.CalendarQuery;
import com.blackcompany.eeos.calendar.application.dto.CalendarResponse;
import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.repository.CalendarRepository;
import com.blackcompany.eeos.calendar.application.usecase.GetCalendarUsecase;
import com.blackcompany.eeos.common.utils.DateUtil;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarQueryService implements GetCalendarUsecase {

	private final CalendarRepository repository;
	private final MemberRepository memberRepository;

	@Override
	public List<CalendarResponse> getCalendar(CalendarQuery query) {
		Integer year = query.year();
		Integer month = query.month();
		Integer date = query.date();
		Integer duration = query.duration();

		boolean dateIsNull = Objects.isNull(date);

		LocalDateTime start = createTargetDate(year, month, dateIsNull ? 1 : date);
		LocalDateTime end =
				createTargetDate(
						year, month, dateIsNull ? DateUtil.getLastDay(year, month) : date + duration);

		return getDefault(start, end);
	}

	@Override
	public List<CalendarResponse> getCalendarForDDay(int DDay) {

		LocalDateTime now = LocalDate.now().atTime(LocalTime.MIDNIGHT);
		LocalDateTime end = now.plusDays(DDay);

		return repository.findNotStarted(now, end).stream().map(this::createResponse).toList();
	}

	private List<CalendarResponse> getDefault(LocalDateTime start, LocalDateTime end) {
		return repository.findByBetweenDate(start, end).stream().map(this::createResponse).toList();
	}

	private CalendarResponse createResponse(CalendarModel model) {
		return CalendarResponse.toResponse(model, getWriterName(model));
	}

	private String getWriterName(CalendarModel model) {
		try {
			return memberRepository.findNameById(model.getWriter());
		} catch (NotFoundMemberException e) {
			throw new IllegalStateException("달력 작성자 이름 매핑 중 에러가 발생했습니다.");
		}
	}

	private LocalDateTime createTargetDate(Integer year, Integer month, Integer date) {
		LocalDateTime result;
		LocalDate localDate;

		if (Objects.isNull(date)) localDate = LocalDate.of(year, month, 1);
		else localDate = LocalDate.of(year, month, date);

		return LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
	}
}
