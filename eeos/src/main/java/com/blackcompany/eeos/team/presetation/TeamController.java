package com.blackcompany.eeos.team.presetation;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.team.application.dto.CreateTeamRequest;
import com.blackcompany.eeos.team.application.dto.CreateTeamResponse;
import com.blackcompany.eeos.team.application.dto.QueryTeamsResponse;
import com.blackcompany.eeos.team.application.usecase.CreateTeamUsecase;
import com.blackcompany.eeos.team.application.usecase.DeleteTeamUsecase;
import com.blackcompany.eeos.team.application.usecase.GetTeamsByActiveStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
@Tag(name = "API", description = "팀생성에 관한 API")
public class TeamController {

	private final CreateTeamUsecase createTeamUsecase;
	private final DeleteTeamUsecase deleteTeamUsecase;

	private final GetTeamsByActiveStatus getTeamsByActiveStatus;

	@Operation(summary = "팀생성", description = "ReqeustBody에 담긴 팀이름으로 팀을 생성한다.")
	@PostMapping
	public ApiResponse<SuccessBody<CreateTeamResponse>> create(
			@Member Long memberId, @RequestBody @Valid CreateTeamRequest request) {
		CreateTeamResponse response = createTeamUsecase.create(memberId, request);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.CREATE);
	}

	@Operation(summary = "팀 삭제", description = "Pathvariable의 teamId을 사용해서 팀을 삭제한다.")
	@DeleteMapping("/{teamId}")
	public ApiResponse<SuccessBody<Void>> delete(
			@Member Long memberId, @PathVariable("teamId") Long teamId) {
		deleteTeamUsecase.delete(memberId, teamId);
		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.DELETE);
	}

	@Operation(summary = "팀 리스트 조회", description = "현학기 활동팀 리스트를 가져온다.")
	@GetMapping
	public ApiResponse<SuccessBody<QueryTeamsResponse>> findTeamsByActiveStatus(
			@RequestParam("programId") String programId) {
		QueryTeamsResponse response = getTeamsByActiveStatus.execute(programId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
	}
}
