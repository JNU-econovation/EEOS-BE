package com.blackcompany.eeos.attend.application.model;

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

	public void validateSame(String status) {
		if (AttendStatus.isSame(status, this.status)) {
			return;
		}
		throw new NotSameBeforeAttendStatusException(memberId);
	}

	public void changeStatus(String status) {
		this.status = AttendStatus.findByAttendStatus(status);
	}
}
