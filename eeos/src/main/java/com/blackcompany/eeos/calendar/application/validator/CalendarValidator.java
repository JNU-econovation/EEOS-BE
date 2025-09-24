package com.blackcompany.eeos.calendar.application.validator;

import com.blackcompany.eeos.calendar.application.exception.DeniedCalendarTypeException;
import com.blackcompany.eeos.calendar.application.exception.DeniedCalendarUpdateException;
import com.blackcompany.eeos.calendar.application.exception.InvalidDateException;
import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.member.application.model.Department;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CalendarValidator {

	private final MemberRepository memberRepository;
	private final Map<CalendarType, Set<Department>> AVAILABLE;

	public CalendarValidator(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;

		AVAILABLE = new HashMap<>();

		final Set<Department> EVENT_AVAILABLE = Set.of(Department.EVENT, Department.PRESIDENT);
		final Set<Department> PRESENTATION_AVAILABLE = Set.of(Department.PRESIDENT);
		final Set<Department> ETC_AVAILABLE =
				Set.of(
						Department.PRESIDENT,
						Department.EVENT,
						Department.MANAGEMENT,
						Department.MARKETING,
						Department.NONE);

		AVAILABLE.put(CalendarType.ETC, ETC_AVAILABLE);
		AVAILABLE.put(CalendarType.PRESENTATION, PRESENTATION_AVAILABLE);
		AVAILABLE.put(CalendarType.EVENT, EVENT_AVAILABLE);
	}

	public void updateValidate(CalendarModel calendar, Long memberId) {
		Department department = memberRepository.findById(memberId).getDepartment();

		if (!isWritable(calendar.getType(), department)) {
			throw new DeniedCalendarUpdateException();
		}
	}

	public void durationValidate(CalendarModel calendar) {
		LocalDateTime startAt = calendar.getStartAt();
		LocalDateTime endAt = calendar.getEndAt();

		if (startAt.isAfter(endAt)) throw new InvalidDateException();
	}

	public void typeValidate(CalendarModel calendar, Department department) {
		CalendarType type = calendar.getType();

		if (!isWritable(type, department)) {
			throw new DeniedCalendarTypeException(department);
		}
	}

	/** 업데이트가 가능한지 여부 : 부서가 업데이트 기준이 된다. */
	private boolean isWritable(CalendarType type, Department department) {
		Set<Department> updatable = AVAILABLE.getOrDefault(type, Set.of());

		if (updatable.contains(department)) return true;
		return false;
	}

	public void urlValidator(CalendarModel calendar) {
		String url = calendar.getUrl();

		// TODO: 정규표현식으로 url 검증
	}
}
