package com.blackcompany.eeos.attend.fixture;

import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.persistence.AttendEntity;

public class AttendFixture {
	public static AttendEntity 참석대상자_엔티티(Long memberId, AttendStatus status) {
		return AttendEntity.builder().memberId(memberId).status(status).build();
	}
}
