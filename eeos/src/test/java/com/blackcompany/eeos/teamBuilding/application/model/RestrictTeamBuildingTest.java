package com.blackcompany.eeos.teamBuilding.application.model;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.blackcompany.eeos.teamBuilding.application.exception.DeniedOverUpperLimitException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestrictTeamBuildingTest {
	@Test
	@DisplayName("활성화된 팀빌딩은 단 1개로 제한된다.")
	void save_team_restrict_active_team() throws InterruptedException, ExecutionException {
		// 초기화
		RestrictTeamBuilding restrictTeamBuilding = RestrictTeamBuilding.of();
		CompletableFuture<Void> future1 =
				CompletableFuture.runAsync(restrictTeamBuilding::addActiveCount);
		CompletableFuture<Void> future2 =
				CompletableFuture.runAsync(restrictTeamBuilding::addActiveCount);

		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2);

		assertThrows(
				DeniedOverUpperLimitException.class,
				() -> {
					try {
						combinedFuture.get();
					} catch (ExecutionException e) {
						throw e.getCause();
					}
				});
	}
}
