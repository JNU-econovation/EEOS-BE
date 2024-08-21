package com.blackcompany.eeos.team.presetation.docs;

import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.dto.CreateTeamResponse;
import com.blackcompany.eeos.team.application.dto.QueryTeamsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;

@Tag(name = "팀", description = "팀 관한 API")
public interface TeamApi {

	@Operation(summary = "팀생성", description = "ReqeustBody에 담긴 팀이름으로 팀을 생성한다.")
	ApiResponse<SuccessBody<CreateTeamResponse>> create(
			@Parameter(hidden = true) Long memberId, @Valid CreateTeamRequest request);

	@Operation(summary = "팀 삭제", description = "Pathvariable의 teamId을 사용해서 팀을 삭제한다.")
	ApiResponse<SuccessBody<Void>> delete(
			@Parameter(hidden = true) Long memberId, @Parameter(description = "삭제할 팀의 ID") Long teamId);

	@Operation(summary = "팀 리스트 조회", description = "현학기 활동팀 리스트를 가져온다.")
	ApiResponse<SuccessBody<QueryTeamsResponse>> findTeamsByActiveStatus(
			@Parameter(description = "프로그램 ID") String programId);
}
