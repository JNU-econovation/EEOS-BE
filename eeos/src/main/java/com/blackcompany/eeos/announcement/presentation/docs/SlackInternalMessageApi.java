package com.blackcompany.eeos.announcement.presentation.docs;

import com.blackcompany.eeos.announcement.application.dto.SaveAnnouncementRequest;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;

public interface SlackInternalMessageApi {

	ApiResponse<SuccessBody<Void>> receiveMessage(SaveAnnouncementRequest request);
}
