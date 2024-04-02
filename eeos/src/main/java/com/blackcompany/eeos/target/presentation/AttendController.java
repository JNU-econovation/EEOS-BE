package com.blackcompany.eeos.target.presentation;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusRequest;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.target.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendAllInfoSortActiveStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendantInfoUsecase;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api")
public class AttendController {

	private final GetAttendantInfoUsecase getAttendantInfoUsecase;
	private final ChangeAttendStatusUsecase changeAttendStatusUsecase;
	private final GetAttendStatusUsecase getAttendStatusUsecase;
	private final GetAttendAllInfoSortActiveStatusUsecase getAttendAllInfoSortActiveStatusUsecase;

	@Operation(summary = "참여여부 조회", description = "프로그램 아이디로 참여자 정보를 불러온다.")
	@GetMapping("/attend/candidate/programs/{programId}")
	public ApiResponse<SuccessBody<List<AttendInfoResponse>>> findAttendMemberInfo(
			@PathVariable("programId") Long programId) {
		List<AttendInfoResponse> response = getAttendantInfoUsecase.findAttendInfo(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Operation(summary = "참여상태 변경", description = "사용자 id와 프로그램 id로 사용자의 beforeAttendStatus 와 afterAttendStatus 를 변경한다.")
	@PutMapping("/attend/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> changeAttendStatus(
			@Member Long memberId,
			@PathVariable("programId") Long programId,
			@RequestBody ChangeAttendStatusRequest request) {
		ChangeAttendStatusResponse response =
				changeAttendStatusUsecase.changeStatus(memberId, request, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@Operation(summary = "참여정보 조회", description = "programId에 해당하는 사용자의 참여 정보를 가져온다.")
	@GetMapping("/attend/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> getAttendStatus(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		ChangeAttendStatusResponse response = getAttendStatusUsecase.getStatus(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
	@Operation(summary = "참여상태 조회", description = "특정 프로그램에 대해 attendStatus 값과 일치하는 참여자 리스트를 불러온다.")
	@GetMapping("/attend/programs/{programId}/members")
	public ApiResponse<SuccessBody<QueryAttendStatusResponse>> getAttendInfoByProgram(
			@PathVariable("programId") Long programId,
			@RequestParam("attendStatus") String attendStatus) {

		QueryAttendStatusResponse response =
				getAttendantInfoUsecase.findAttendInfo(programId, attendStatus);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
	@Operation(summary = "activeStatus 별로 회원 리스트 조회", description = "프로그램에 관련된 회원들을 activeStatus에 맞게 가져온다.")
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
