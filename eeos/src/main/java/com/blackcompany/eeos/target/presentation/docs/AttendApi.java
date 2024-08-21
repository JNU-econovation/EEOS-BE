package com.blackcompany.eeos.target.presentation.docs;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.target.application.dto.ChangeAttendStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendActiveStatusResponse;
import com.blackcompany.eeos.target.application.dto.QueryAttendStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@Tag(name = "행사 참여", description = "행사 참여 관련 API")
public interface AttendApi {

	@Operation(summary = "참여여부 조회", description = "PathVaiable 에 담긴 programId를 사용하여 참여자 리스트를 불러온다.")
	ApiResponse<SuccessBody<List<AttendInfoResponse>>> findAttendMemberInfo(Long programId);

	@Operation(
			summary = "참여상태 변경",
			description = "PathVariable의 programId와 RequestBody의 참여상태를 이용하여 사용자의 참여상태를 변경한다.")
	ApiResponse<SuccessBody<ChangeAttendStatusResponse>> changeAttendStatus(
			@Parameter(hidden = true) Long memberId, Long programId);

	@Operation(
			summary = "참여정보 조회",
			description = "MemberId와 PathVariable에 담긴 programId를 이용해 특정 프로그램에 해당하는 사용자의 참여 정보를 가져온다.")
	ApiResponse<SuccessBody<ChangeAttendStatusResponse>> getAttendStatus(
			@Parameter(hidden = true) Long memberId, Long programId);

	@Operation(
			summary = "참여상태 조회(참석, 불참, 지각)",
			description =
					"PathVariable에 담긴 programId와 RequestParam에 담긴 attendStatus 를 이용해 특정 프로그램에 대해 활동상태에 맞는 참여자 리스트를 불러온다.")
	ApiResponse<SuccessBody<QueryAttendStatusResponse>> getAttendInfoByProgram(
			Long programId, String attendStatus);

	@Operation(
			summary = "회원 활동상태별 참여정보 조회(RM, CM, OB)",
			description =
					"PathVariable에 담긴 프로그램 정보와 RequestParam에 담긴 activeStatus를 이용해 프로그램의 참석정보를 회원상태 기준으로 불러온다.")
	ApiResponse<SuccessBody<QueryAttendActiveStatusResponse>>
			getAttendAllInfoByProgramSortActiveStatus(Long programId, String activeStatus);
}
