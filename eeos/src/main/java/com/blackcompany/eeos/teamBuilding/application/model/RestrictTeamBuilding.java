package com.blackcompany.eeos.teamBuilding.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.teamBuilding.application.exception.DeniedOverUpperLimitException;
import com.blackcompany.eeos.teamBuilding.application.exception.NotFoundProgressTeamBuildingException;
import java.util.concurrent.atomic.AtomicLong;
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
public class RestrictTeamBuilding implements AbstractModel {
	private static final AtomicLong UPPER_LIMIT = new AtomicLong(1);
	private static final AtomicLong LOWER_LIMIT = new AtomicLong(0);
	private AtomicLong totalActiveCount;

	public void addActiveCount() {
		totalActiveCount.incrementAndGet();
		validateCreation();
	}

	public void subtractActiveCount() {
		totalActiveCount.decrementAndGet();
		validateDeletion();
	}

	private void validateCreation() {
		if (totalActiveCount.get() > UPPER_LIMIT.get()) {
			throw new DeniedOverUpperLimitException();
		}
	}

	private void validateDeletion() {
		if (totalActiveCount.get() < LOWER_LIMIT.get()) {
			throw new NotFoundProgressTeamBuildingException();
		}
	}

	public static RestrictTeamBuilding of() {
		return RestrictTeamBuilding.builder().totalActiveCount(new AtomicLong()).build();
	}
}
