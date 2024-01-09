package com.blackcompany.eeos.attend.application.model;

import com.blackcompany.eeos.attend.application.exception.DeniedSaveAttendException;
import com.blackcompany.eeos.attend.application.exception.NotSameBeforeAttendStatusException;
import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AttendModel implements AbstractModel {
	private Long id;
	private Long memberId;
	private Long programId;
	private AttendStatus status;

	public AttendModel changeStatus(String beforeStatus, String afterStatus) {
		validateChange(beforeStatus);
		this.status = AttendStatus.find(afterStatus);

		return this;
	}

	public String getStatus() {
		return status.getStatus();
	}

	public static AttendModel of() {
		return AttendModel.builder().status(AttendStatus.NONRELATED).build();
	}

	private void validateChange(String beforeStatus) {
		canChange(beforeStatus);
		isSameBeforeStatus(beforeStatus);
	}

	private void canChange(String beforeStatus) {
		if (AttendStatus.isSame(beforeStatus, AttendStatus.NONRELATED)) {
			throw new DeniedSaveAttendException();
		}
	}

	private void isSameBeforeStatus(String status) {
		if (AttendStatus.isSame(status, this.status)) {
			return;
		}
		throw new NotSameBeforeAttendStatusException(memberId);
	}
}
