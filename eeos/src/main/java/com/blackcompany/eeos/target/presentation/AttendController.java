package com.blackcompany.eeos.target.presentation;

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
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api")
public class AttendController {

	private final GetAttendantInfoUsecase getAttendantInfoUsecase;
	private final ChangeAttendStatusUsecase changeAttendStatusUsecase;
	private final GetAttendStatusUsecase getAttendStatusUsecase;
	private final GetAttendAllInfoSortActiveStatusUsecase getAttendAllInfoSortActiveStatusUsecase;

	@Operation(summary = "참여여부 조회", description = "PathVaiable 에 담긴 programId를 사용하여 참여자 리스트를 불러온다.")
	@GetMapping("/attend/candidate/programs/{programId}")
	public ApiResponse<SuccessBody<List<AttendInfoResponse>>> findAttendMemberInfo(
			@PathVariable("programId") Long programId) {
		List<AttendInfoResponse> response = getAttendantInfoUsecase.findAttendInfo(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Operation(
			summary = "참여상태 변경",
			description = "PathVariable의 programId와 RequestBody의 참여상태를 이용하여 사용자의 참여상태를 변경한다.")
	@PostMapping("/attend/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> changeAttendStatus(
			@Member Long memberId,
			@PathVariable("programId") Long programId) {
		ChangeAttendStatusResponse response =
				changeAttendStatusUsecase.changeStatus(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.UPDATE);
	}

	@Operation(
			summary = "참여정보 조회",
			description = "MemberId와 PathVariable에 담긴 programId를 이용해 특정 프로그램에 해당하는 사용자의 참여 정보를 가져온다.")
	@GetMapping("/attend/programs/{programId}")
	public ApiResponse<SuccessBody<ChangeAttendStatusResponse>> getAttendStatus(
			@Member Long memberId, @PathVariable("programId") Long programId) {
		ChangeAttendStatusResponse response = getAttendStatusUsecase.getStatus(memberId, programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Operation(
			summary = "참여상태 조회(참석, 불참, 지각)",
			description =
					"PathVariable에 담긴 programId와 RequestParam에 담긴 attendStatus 를 이용해 특정 프로그램에 대해 활동상태에 맞는 참여자 리스트를 불러온다.")
	@GetMapping("/attend/programs/{programId}/members")
	public ApiResponse<SuccessBody<QueryAttendStatusResponse>> getAttendInfoByProgram(
			@PathVariable("programId") Long programId,
			@RequestParam("attendStatus") String attendStatus) {

		QueryAttendStatusResponse response =
				getAttendantInfoUsecase.findAttendInfo(programId, attendStatus);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Operation(
			summary = "회원 활동상태별 참여정보 조회(RM, CM, OB)",
			description =
					"PathVariable에 담긴 프로그램 정보와 RequestParam에 담긴 activeStatus를 이용해 프로그램의 참석정보를 회원상태 기준으로 불러온다.")
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
