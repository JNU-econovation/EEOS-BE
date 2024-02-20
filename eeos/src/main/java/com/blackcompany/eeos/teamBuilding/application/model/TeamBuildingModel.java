package com.blackcompany.eeos.teamBuilding.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.target.application.exception.DeniedSaveAttendTeamBuildingException;
import com.blackcompany.eeos.teamBuilding.application.exception.DeniedEditTeamBuilding;
import com.blackcompany.eeos.teamBuilding.persistence.TeamBuildingStatus;
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
public class TeamBuildingModel implements AbstractModel {
	private Long id;

	private String title;

	private String content;

	private int maxTeamSize;
	private String status;
	private Long memberId;

	public void validateEdit(Long memberId) {
		if (!this.memberId.equals(memberId)) {
			throw new DeniedEditTeamBuilding();
		}
	}

	public TeamBuildingModel updateStatus(String status, Long memberId) {
		validateEdit(memberId);

		this.status = status;
		return this;
	}

	public void validateAttend() {
		validateStatus();
	}

	public TeamBuildingAccessRights getAccessRight(Long memberId) {
		if (this.memberId.equals(memberId)) {
			return TeamBuildingAccessRights.EDIT;
		}
		return TeamBuildingAccessRights.READ_ONLY;
	}

	private void validateStatus() {
		if (!TeamBuildingStatus.validateSame(status, TeamBuildingStatus.PROGRESS)) {
			throw new DeniedSaveAttendTeamBuildingException();
		}
	}
}
