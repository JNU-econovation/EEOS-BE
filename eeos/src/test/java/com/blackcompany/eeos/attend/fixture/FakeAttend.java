package com.blackcompany.eeos.attend.fixture;

import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.attend.persistence.AttendEntity;

public class FakeAttend {

	public static AttendEntity attendMandoEntity() {
		return AttendEntity.builder().id(1L).memberId(1L).status(AttendStatus.ATTEND).build();
	}

	public static AttendEntity attendBadaEntity() {
		return AttendEntity.builder().id(2L).memberId(2L).status(AttendStatus.ATTEND).build();
	}
}
