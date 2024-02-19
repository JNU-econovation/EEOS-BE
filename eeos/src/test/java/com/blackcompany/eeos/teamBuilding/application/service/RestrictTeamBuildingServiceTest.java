package com.blackcompany.eeos.teamBuilding.application.service;

import static org.junit.jupiter.api.Assertions.*;

import com.blackcompany.eeos.teamBuilding.application.exception.DeniedOverUpperLimitException;
import com.blackcompany.eeos.teamBuilding.application.model.RestrictTeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.application.model.converter.RestrictTeamBuildingConverter;
import com.blackcompany.eeos.teamBuilding.fixture.TeamBuildingFixture;
import com.blackcompany.eeos.teamBuilding.persistence.RestrictTeamBuildingRepository;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestrictTeamBuildingServiceTest {
	@Mock RestrictTeamBuildingRepository restrictTeamBuildingRepository;

	@Mock RestrictTeamBuildingConverter restrictTeamBuildingConverter;

	@InjectMocks RestrictTeamBuildingService restrictTeamBuildingService;

	@Test
	@DisplayName("활성화된 팀빌딩은 단 1개로 제한된다.")
	@Disabled
	void save_team_restrict_active_team() {
		// given
		RestrictTeamBuildingModel 진행중인_팀빌딩_없음 = TeamBuildingFixture.진행중인_팀빌딩_없음();

		// when
		CompletableFuture<Void> future1 =
				CompletableFuture.runAsync(restrictTeamBuildingService::addTeamBuilding);
		CompletableFuture<Void> future2 =
				CompletableFuture.runAsync(restrictTeamBuildingService::addTeamBuilding);

		// then
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
