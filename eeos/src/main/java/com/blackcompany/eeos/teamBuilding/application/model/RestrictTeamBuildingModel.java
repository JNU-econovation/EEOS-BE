package com.blackcompany.eeos.teamBuilding.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.teamBuilding.application.exception.DeniedOverUpperLimitException;
import com.blackcompany.eeos.teamBuilding.application.exception.NotFoundTeamBuildingStatusException;
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
public class RestrictTeamBuildingModel implements AbstractModel {
	private static final Long UPPER_LIMIT = 1L;
	private static final Long LOWER_LIMIT = 0L;

	private Long id;
	private Long totalActiveCount;
	private Long version;

	public RestrictTeamBuildingModel addActiveCount() {
		totalActiveCount++;
		validateCreation();

		return this;
	}

	public RestrictTeamBuildingModel subtractActiveCount() {
		totalActiveCount--;
		validateDeletion();

		return this;
	}

	public Accessibility getCreatableAccessibility() {
		if (isOver()) {
			return Accessibility.NONCREATABLE;
		}
		return Accessibility.CREATABLE;
	}

	private void validateCreation() {
		if (isOver()) {
			throw new DeniedOverUpperLimitException();
		}
	}

	private void validateDeletion() {
		if (totalActiveCount < LOWER_LIMIT) {
			throw new NotFoundTeamBuildingStatusException(TeamBuildingStatus.PROGRESS.getStatus());
		}
	}

	private boolean isOver() {
		return totalActiveCount >= UPPER_LIMIT;
	}
}
