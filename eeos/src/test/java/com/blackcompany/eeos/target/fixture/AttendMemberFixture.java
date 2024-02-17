package com.blackcompany.eeos.target.fixture;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.persistence.AttendEntity;
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
