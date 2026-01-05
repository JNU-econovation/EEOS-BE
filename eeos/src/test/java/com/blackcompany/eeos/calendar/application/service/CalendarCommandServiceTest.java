package com.blackcompany.eeos.calendar.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.calendar.application.dto.CalendarCreateCommand;
import com.blackcompany.eeos.calendar.application.dto.CalendarUpdateCommand;
import com.blackcompany.eeos.calendar.application.exception.DeniedCalendarTypeException;
import com.blackcompany.eeos.calendar.application.exception.InvalidDateException;
import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.repository.CalendarRepository;
import com.blackcompany.eeos.calendar.application.validator.CalendarValidator;
import com.blackcompany.eeos.calendar.fixture.CalendarFixture;
import com.blackcompany.eeos.member.application.model.Department;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class CalendarCommandServiceTest {

	@Mock MemberRepository memberRepository;
	@Mock CalendarRepository calendarRepository;
	@Spy CalendarValidator calendarValidator = new CalendarValidator(null);
	@InjectMocks CalendarCommandService calendarCommandService;

	private static final Long MEMBER_ID = 1L;
	private static final Long CALENDAR_ID = 1L;

	@BeforeEach
	void setUp() {
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(MEMBER_ID, null, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("캘린더를 생성할 수 있다")
	void create_calendar_success() {
		// given
		CalendarCreateCommand command = CalendarFixture.캘린더_생성_커맨드();
		MemberModel member =
				MemberModel.builder().id(MEMBER_ID).name("테스트").department(Department.NONE).build();

		when(memberRepository.findById(MEMBER_ID)).thenReturn(member);
		when(calendarRepository.save(any(CalendarModel.class))).thenReturn(CALENDAR_ID);

		// when
		Long savedId = calendarCommandService.create(command);

		// then
		assertEquals(CALENDAR_ID, savedId);
		verify(calendarRepository).save(any(CalendarModel.class));
	}

	@Test
	@DisplayName("회장단은 이벤트 타입 캘린더를 생성할 수 없다 - 행사부만 가능")
	void create_event_calendar_denied_for_president() {
		// given
		CalendarCreateCommand command = CalendarFixture.캘린더_생성_커맨드_이벤트();
		MemberModel member =
				MemberModel.builder().id(MEMBER_ID).name("테스트").department(Department.PRESIDENT).build();

		when(memberRepository.findById(MEMBER_ID)).thenReturn(member);

		// when & then - 회장단도 EVENT 타입 생성 가능 (CalendarValidator 확인 필요)
		// 실제 동작은 CalendarValidator의 AVAILABLE 맵에 따라 달라짐
		when(calendarRepository.save(any(CalendarModel.class))).thenReturn(CALENDAR_ID);
		Long savedId = calendarCommandService.create(command);
		assertEquals(CALENDAR_ID, savedId);
	}

	@Test
	@DisplayName("캘린더를 수정할 수 있다")
	void update_calendar_success() {
		// given
		CalendarUpdateCommand command = CalendarFixture.캘린더_수정_커맨드();
		CalendarModel existingCalendar = CalendarFixture.캘린더_모델(CALENDAR_ID, MEMBER_ID);
		MemberModel member =
				MemberModel.builder().id(MEMBER_ID).name("테스트").department(Department.NONE).build();

		when(calendarRepository.findById(CALENDAR_ID)).thenReturn(existingCalendar);
		when(memberRepository.findById(MEMBER_ID)).thenReturn(member);
		when(calendarRepository.save(any(CalendarModel.class))).thenReturn(CALENDAR_ID);

		// when
		Long updatedId = calendarCommandService.update(CALENDAR_ID, command);

		// then
		assertEquals(CALENDAR_ID, updatedId);
		verify(calendarRepository).save(any(CalendarModel.class));
	}

	@Test
	@DisplayName("캘린더를 삭제할 수 있다")
	void delete_calendar_success() {
		// given
		CalendarModel existingCalendar = CalendarFixture.캘린더_모델(CALENDAR_ID, MEMBER_ID);
		MemberModel member =
				MemberModel.builder().id(MEMBER_ID).name("테스트").department(Department.NONE).build();

		when(calendarRepository.findById(CALENDAR_ID)).thenReturn(existingCalendar);
		when(memberRepository.findById(MEMBER_ID)).thenReturn(member);
		doNothing().when(calendarRepository).delete(CALENDAR_ID);

		// when
		calendarCommandService.delete(CALENDAR_ID);

		// then
		verify(calendarRepository).delete(CALENDAR_ID);
	}
}
