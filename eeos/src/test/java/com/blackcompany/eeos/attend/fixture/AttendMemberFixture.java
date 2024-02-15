package com.blackcompany.eeos.attend.fixture;

import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.persistence.AttendEntity;
import java.util.List;

public class AttendMemberFixture {
	public static List<AttendEntity> 수민_바다_참석() {
		return List.of(수민_참석(), 바다_참석());
	}

	public static AttendEntity 수민_참석() {
		return AttendEntity.builder().id(1L).memberId(1L).status(AttendStatus.ATTEND).build();
	}

	public static AttendEntity 바다_참석() {
		return AttendEntity.builder().id(2L).memberId(2L).status(AttendStatus.ATTEND).build();
	}
}
