package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.program.application.dto.ChangeAllAttendStatusRequest;
import java.util.List;

public interface AttendTargetService {
	/**
	 * 참여 대상자의 참여 정보를 변경한다.
	 *
	 * @param programId
	 * @param requests
	 */
	void update(final Long programId, final List<ChangeAllAttendStatusRequest> requests);
}
