package com.blackcompany.eeos.teamBuilding.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.teamBuilding.application.exception.DeniedOverUpperLimitException;
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
	private AtomicLong totalActiveCount;

	public void addActiveCount() {
		totalActiveCount.incrementAndGet();
		validateCreation();
	}

	private void validateCreation() {
		if (totalActiveCount.get() > UPPER_LIMIT.get()) {
			System.out.println("실패" + totalActiveCount.get());
			throw new DeniedOverUpperLimitException();
		}

		System.out.println("성공" + totalActiveCount.get());
	}

	public static RestrictTeamBuilding of() {
		return RestrictTeamBuilding.builder().totalActiveCount(new AtomicLong()).build();
	}
}
