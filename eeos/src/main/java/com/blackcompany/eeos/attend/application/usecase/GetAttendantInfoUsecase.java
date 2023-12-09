package com.blackcompany.eeos.attend.application.usecase;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import java.util.List;

public interface GetAttendantInfoUsecase {
	List<AttendInfoResponse> findAttendInfo(final Long programId);

	List<AttendInfoResponse> findAttendInfo(final Long programId, final String attendStatus);
}
