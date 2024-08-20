package com.blackcompany.eeos.member.presentation.docs;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.member.application.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "멤버", description = "멤버에 관한 API")
public interface MemberApi {

	@Operation(
			summary = "관리자_회원 상태 변경",
			description = "RequestBody의 activeStatus를 사용해 회원 상태를 AM,RM,CM,OB 중 하나로 변경한다.")
	ApiResponse<SuccessBody<CommandMemberResponse>> adminChangeActiveStatus(
			@Parameter(hidden = true) @Member Long adminMemberId,
			@PathVariable("memberId") Long memberId,
			@RequestBody ChangeActiveStatusRequest request);

	@Operation(summary = "관리자_회원 삭제", description = "Pathvariable의 memberId를 사용해 회원을 삭제합니다.")
	ApiResponse<SuccessBody<Void>> delete(
			@Parameter(hidden = true) @Member Long adminMemberId,
			@PathVariable("memberId") Long memberId);

	@Operation(
			summary = "활동 상태와 일치하는 회원 리스트를 가져온다.",
			description = "Request Parameter 값으로 전달 받은 활동 상태와 동일한 회원 리스트를 가져온다.")
	ApiResponse<SuccessBody<QueryMembersResponse>> findMembersByActiveStatus(
			@RequestParam("activeStatus") String activeStatus);

	@Operation(summary = "본인(회원 1인)의 활동 상태 가져오기", description = "액세스 토큰에서 회원 ID를 추출하여 회원 상태를 가져온다.")
	ApiResponse<SuccessBody<QueryMemberResponse>> findMemberByActiveStatus(
			@Parameter(hidden = true) @Member Long memberId);
}
