package com.blackcompany.eeos.target.fixture;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.persistence.AttendEntity;

public class AttendFixture {
	public static AttendEntity 참석대상자_엔티티(Long memberId, AttendStatus status) {
		return AttendEntity.builder().memberId(memberId).status(status).build();
	}
}
