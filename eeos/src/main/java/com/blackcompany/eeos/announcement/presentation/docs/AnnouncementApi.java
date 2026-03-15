package com.blackcompany.eeos.announcement.presentation.docs;

import com.blackcompany.eeos.announcement.application.dto.GetAnnouncementsResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;

public interface AnnouncementApi {

	ApiResponse<SuccessBody<GetAnnouncementsResponse>> getAnnouncements();
}
