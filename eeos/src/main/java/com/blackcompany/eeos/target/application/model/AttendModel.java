package com.blackcompany.eeos.target.application.model;

import com.blackcompany.eeos.common.application.model.MemberIdModel;
import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.target.application.exception.DeniedChangeAttendException;
import com.blackcompany.eeos.target.application.exception.DeniedSaveAttendException;
import com.blackcompany.eeos.target.application.exception.NotSameBeforeAttendStatusException;
import java.util.List;
import java.util.stream.Collectors;
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
public class AttendModel implements AbstractModel, MemberIdModel {
	private Long id;
	private Long memberId;
	private Long programId;
	private AttendStatus status;

	public AttendModel changeStatus(String afterStatus) {
		validateChange(afterStatus);
		this.status = AttendStatus.find(afterStatus);

		return this;
	}

	public AttendModel changeStatusByManager(String beforeStatus, String afterStatus) {
		validateChangeByManager(beforeStatus);
		this.status = AttendStatus.find(afterStatus);

		return this;
	}

	public String getStatus() {
		return status.getStatus();
	}

	public static AttendModel of() {
		return AttendModel.builder().status(AttendStatus.NONRELATED).build();
	}

	public static List<AttendModel> of(List<Long> memberIds, Long programId) {
		return memberIds.stream().map(memberId -> of(memberId, programId)).collect(Collectors.toList());
	}

	public static AttendModel of(Long memberId, Long programId) {
		return AttendModel.builder()
				.memberId(memberId)
				.programId(programId)
				.status(AttendStatus.NONRELATED)
				.build();
	}

	private void validateChange(String afterStatus) {
		canChange();
		isSameBeforeStatus(afterStatus);
	}

	private void validateChangeByManager(String beforeStatus) {
		isSameBeforeStatus(beforeStatus);
	}

	private void canChange() {
		if (AttendStatus.isSame(status.getStatus(), AttendStatus.NONRELATED)) {
			throw new DeniedSaveAttendException();
		}

		if (!AttendStatus.isSame(status.getStatus(), AttendStatus.NONRESPONSE)) {
			throw new DeniedChangeAttendException();
		}
	}

	private void isSameBeforeStatus(String status) {
		if (!AttendStatus.isSame(status, this.status)) {
			return;
		}
		throw new NotSameBeforeAttendStatusException(memberId);
	}
}
