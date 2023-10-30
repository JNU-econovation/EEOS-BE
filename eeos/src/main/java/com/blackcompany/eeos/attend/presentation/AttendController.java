package com.blackcompany.eeos.attend.presentation;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.ChangeStatusRequest;
import com.blackcompany.eeos.attend.application.usecase.ChangeStatusUsecase;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attend")
public class AttendController {

	private final GetAttendantInfoUsecase getAttendantInfoUsecase;
	private final ChangeStatusUsecase changeStatusUsecase;

	@GetMapping("/candidate/program/{programId}")
	public ApiResponse<SuccessBody<List<AttendInfoResponse>>> findAttendMemberInfo(
			@PathVariable("programId") Long programId) {
		List<AttendInfoResponse> response = getAttendantInfoUsecase.findAttendInfo(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@PostMapping("/programs/{programId}")
	public ApiResponse<SuccessBody<Void>> changeAttendStatus(
			@PathVariable("programId") Long programId, @RequestBody ChangeStatusRequest request) {
		changeStatusUsecase.changeStatus(request, programId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.UPDATE);
	}

	@GetMapping("/programs/{programId}/members")
	public ApiResponse<SuccessBody<List<AttendInfoResponse>>> getAttendInfoByProgram(
			@PathVariable("programId") Long programId, @RequestParam("status") String status) {
		List<AttendInfoResponse> response = getAttendantInfoUsecase.findAttendInfo(programId, status);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
