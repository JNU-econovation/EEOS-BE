package com.blackcompany.eeos.program.application.usecase;

public interface AttendModeChangeUsecase {

	void changeMode(Long memberId, Long programId, String mode);
}
