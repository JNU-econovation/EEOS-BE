package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.ProgramSlackNotificationRequest;

public interface NotifyProgramUsecase {

    CommandProgramResponse notify(Long memberId, Long programId, ProgramSlackNotificationRequest request);

}
