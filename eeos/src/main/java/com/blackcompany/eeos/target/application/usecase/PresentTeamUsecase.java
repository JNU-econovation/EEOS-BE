package com.blackcompany.eeos.target.application.usecase;

import java.util.List;

public interface PresentTeamUsecase {

	void save(Long programId, List<Long> teamIds);
}
