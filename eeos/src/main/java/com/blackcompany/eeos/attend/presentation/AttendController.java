package com.blackcompany.eeos.attend.presentation;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.attend.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.attend.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.attend.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.attend.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attend")
public class AttendController {

	private final GetAttendantInfoUsecase getAttendantInfoUsecase;
	private final ChangeAttendStatusUsecase changeAttendStatusUsecase;
	private final GetAttendStatusUsecase getAttendStatusUsecase;

	@GetMapping("/candidate/programs/{programId}")
	public ApiResponse<SuccessBody<List<AttendInfoResponse>>> findAttendMemberInfo(
			@PathVariable("programId") Long programId) {
		List<AttendInfoResponse> response = getAttendantInfoUsecase.findAttendInfo(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@PutMapping("/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> changeAttendStatus(
			@Member Long memberId,
			@PathVariable("programId") Long programId,
			@RequestBody ChangeAttendStatusRequest request) {
		ChangeAttendStatusResponse response =
				changeAttendStatusUsecase.changeStatus(memberId, request, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@GetMapping("/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> getAttendStatus(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		ChangeAttendStatusResponse response = getAttendStatusUsecase.getStatus(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@GetMapping("/programs/{programId}/members")
	public ApiResponse<SuccessBody<QueryAttendStatusResponse>> getAttendInfoByProgram(
			@PathVariable("programId") Long programId, @RequestParam("attendStatus") String status) {
		QueryAttendStatusResponse response = getAttendantInfoUsecase.findAttendInfo(programId, status);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
