package com.blackcompany.eeos.target.presentation.controller;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.response.ApiResponse;
import com.blackcompany.eeos.common.presentation.response.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.response.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.response.MessageCode;
import com.blackcompany.eeos.common.presentation.response.PageResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfoWithProgramResponse;
import com.blackcompany.eeos.target.application.dto.AttendInfosSearchRequest;
import com.blackcompany.eeos.target.application.dto.AttendPenaltyRankingResponse;
import com.blackcompany.eeos.target.application.dto.AttendPenaltyResponse;
import com.blackcompany.eeos.target.application.dto.AttendSummaryInfoResponse;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.PenaltyInfoRequest;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import com.blackcompany.eeos.target.application.usecase.ChangeAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendAllInfoSortActiveStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendStatusUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendantInfoUsecase;
import com.blackcompany.eeos.target.presentation.docs.AttendApi;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@Override
	@GetMapping("/attend/programs/fire-finger/{programId}")
	public ApiResponse<SuccessBody<QueryAttendStatusResponse>> getAttendInfoByTop5(
			@PathVariable("programId") Long programId) {
		QueryAttendStatusResponse response = getAttendantInfoUsecase.findFireFingerMembers(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@GetMapping("/attend/programs")
	@Override
	public ApiResponse<SuccessBody<PageResponse<AttendInfoWithProgramResponse>>>
			getMyAttendInfosWithProgram(@Valid AttendInfosSearchRequest request) {
		PageResponse<AttendInfoWithProgramResponse> responses =
				getAttendantInfoUsecase.findMyAttendInfo(
						request.getPage(), request.getSize(), request.getStartDate(), request.getEndDate());
		return ApiResponseGenerator.success(responses, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@GetMapping("/attend/penalties")
	public ApiResponse<SuccessBody<PageResponse<AttendPenaltyResponse>>> getPenaltyInfo(
			PenaltyInfoRequest request) {
		PageResponse<AttendPenaltyResponse> responses =
				getAttendantInfoUsecase.getPenaltyInfos(
						request.page(),
						request.size(),
						request.sortType(),
						request.startDate(),
						request.endDate());

		return ApiResponseGenerator.success(responses, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@GetMapping("/attend/summary")
	public ApiResponse<SuccessBody<AttendSummaryInfoResponse>> getMyAttendSummaryInfo(
			@RequestParam(value = "startDate", required = false) Long startDate,
			@RequestParam(value = "endDate", required = false) Long endDate) {
		AttendSummaryInfoResponse response =
				getAttendantInfoUsecase.getMyAttendSummary(startDate, endDate);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}

	@Override
	@GetMapping("/attend/penalty")
	public ApiResponse<SuccessBody<AttendPenaltyRankingResponse>> getMyPenaltyRankingInfo(
			@RequestParam("offset") int rankOffset) {
		AttendPenaltyRankingResponse response = getAttendantInfoUsecase.getMyPenaltyRanking(rankOffset);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
