package com.blackcompany.eeos.teamBuilding.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.teamBuilding.application.exception.DeniedEditTeamBuilding;
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
}
