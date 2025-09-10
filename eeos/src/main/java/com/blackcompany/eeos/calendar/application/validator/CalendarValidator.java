package com.blackcompany.eeos.calendar.application.validator;

import com.blackcompany.eeos.calendar.application.exception.DeniedCalendarTypeException;
import com.blackcompany.eeos.calendar.application.exception.DeniedCalendarUpdateException;
import com.blackcompany.eeos.calendar.application.exception.InvalidDateException;
import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import com.blackcompany.eeos.member.application.model.Department;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CalendarValidator {

	private final Map<CalendarType, Set<Department>> AVAILABLE = new HashMap<>();

	public CalendarValidator() {
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

	public void updateValidate(CalendarModel calendar, Long memberId){
		if(!calendar.isWriter(memberId)) throw new DeniedCalendarUpdateException();
	}

	public void durationValidate(CalendarModel calendar) {
		LocalDateTime startAt = calendar.getStartAt();
		LocalDateTime endAt = calendar.getEndAt();

		if (startAt.isAfter(endAt)) throw new InvalidDateException();
	}

	public void typeValidate(CalendarModel calendar, Department department) {
		CalendarType type = calendar.getType();
		Set<Department> departments = AVAILABLE.get(type);

		if (!departments.contains(department)) {
			throw new DeniedCalendarTypeException(department);
		}
	}

	public void urlValidator(CalendarModel calendar) {
		String url = calendar.getUrl();

		// TODO: 정규표현식으로 url 검증
	}
}
