package com.blackcompany.eeos.attend.application.model;

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

	public void validateBeforeStatus(String beforeStatus) {
		this.status.isEqualsStatus(beforeStatus);
	}

	public void changeStatus(String afterStatus) {
		this.status = AttendStatus.findStatus(afterStatus);
	}
}
