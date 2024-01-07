package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendStatusResponse;
import java.util.List;

public interface GetAttendantInfoUsecase {
	List<AttendInfoResponse> findAttendInfo(final Long programId);

	QueryAttendStatusResponse findAttendInfo(final Long programId, final String attendStatus);
}
