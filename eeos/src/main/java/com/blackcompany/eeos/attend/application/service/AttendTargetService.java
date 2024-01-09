package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.program.application.dto.ChangeAllAttendStatusRequest;
import com.blackcompany.eeos.program.application.dto.ProgramMembers;
import java.util.List;

/** 관련 있는 대상자를 선택한다. */
public interface AttendTargetService {
	void save(final Long programId, final List<ProgramMembers> members);

	void update(final Long programId, final List<ChangeAllAttendStatusRequest> requests);
}
