package com.blackcompany.eeos.attend.presentation;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attend/candidate")
public class AttendController {
	private final GetAttendantInfoUsecase getAttendantInfoUsecase;

	@GetMapping("/program/{programId}")
	public ApiResponse<SuccessBody<List<AttendInfoResponse>>> findAttendMemberInfo(
			@PathVariable("programId") Long programId) {
		List<AttendInfoResponse> response = getAttendantInfoUsecase.findAttendInfo(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
