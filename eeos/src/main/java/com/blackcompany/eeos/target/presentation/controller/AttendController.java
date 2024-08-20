package com.blackcompany.eeos.target.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.target.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendAllInfoSortActiveStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.target.presentation.docs.AttendApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AttendController implements AttendApi {

	private final GetAttendantInfoUsecase getAttendantInfoUsecase;
	private final ChangeAttendStatusUsecase changeAttendStatusUsecase;
	private final GetAttendStatusUsecase getAttendStatusUsecase;
	private final GetAttendAllInfoSortActiveStatusUsecase getAttendAllInfoSortActiveStatusUsecase;

	@Override
	@GetMapping("/attend/candidate/programs/{programId}")
	public ApiResponse<SuccessBody<List<AttendInfoResponse>>> findAttendMemberInfo(
			@PathVariable("programId") Long programId) {
		List<AttendInfoResponse> response = getAttendantInfoUsecase.findAttendInfo(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@PostMapping("/attend/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> changeAttendStatus(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		ChangeAttendStatusResponse response =
				changeAttendStatusUsecase.changeStatus(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@Override
	@GetMapping("/attend/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> getAttendStatus(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		ChangeAttendStatusResponse response = getAttendStatusUsecase.getStatus(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@GetMapping("/attend/programs/{programId}/members")
	public ApiResponse<SuccessBody<QueryAttendStatusResponse>> getAttendInfoByProgram(
			@PathVariable("programId") Long programId,
			@RequestParam("attendStatus") String attendStatus) {

		QueryAttendStatusResponse response =
				getAttendantInfoUsecase.findAttendInfo(programId, attendStatus);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@GetMapping("/programs/{programId}/members")
	public ApiResponse<SuccessBody<QueryAttendActiveStatusResponse>>
			getAttendAllInfoByProgramSortActiveStatus(
					@PathVariable("programId") Long programId,
					@RequestParam("activeStatus") String activeStatus) {
		QueryAttendActiveStatusResponse response =
				getAttendAllInfoSortActiveStatusUsecase.getAttendInfo(programId, activeStatus);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
