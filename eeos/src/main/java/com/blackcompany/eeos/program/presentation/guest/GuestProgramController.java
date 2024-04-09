package com.blackcompany.eeos.program.presentation.guest;

import com.blackcompany.eeos.auth.presentation.support.Member;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponse;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseBody.SuccessBody;
import com.blackcompany.eeos.common.presentation.respnose.ApiResponseGenerator;
import com.blackcompany.eeos.common.presentation.respnose.MessageCode;
import com.blackcompany.eeos.program.application.dto.PageResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;
import com.blackcompany.eeos.program.application.dto.QueryProgramsResponse;
import com.blackcompany.eeos.program.application.usecase.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/guest/programs")
@Tag(name = "게스트모드 API", description = "게스트모드에 관한 API")
public class GuestProgramController {
    private final CreateProgramUsecase createProgramUsecase;
    private final GetProgramUsecase getProgramUsecase;
    private final UpdateProgramUsecase updateProgramUsecase;
    private final GetProgramsUsecase getProgramsUsecase;
    private final DeleteProgramUsecase deleteProgramUsecase;
    private final GetAccessRightUsecase getAccessRightUsecase;

    @Operation(summary = "게스트모드 - 행사 리스트 조회", description = "RequestParam에 담긴 category, programStatus, size, page를 이용해 프로그램 리스트를 조회한다.")
    @GetMapping()
    public ApiResponse<SuccessBody<PageResponse<QueryProgramsResponse>>> findAll(
            @RequestParam("category") String category,
            @RequestParam("programStatus") String status,
            @RequestParam("size") int size,
            @RequestParam("page") int page) {
        PageResponse<QueryProgramsResponse> response =
                getProgramsUsecase.getPrograms(category, status, size, page);
        return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
    }

    @Operation(summary = "행사 조회", description = "PathVariable에 담긴 prgramId를 이용해 특정 프로그램의 내용을 불러온다. 참석, 불참, 지각의 정보는 블라인트 처리된다.")
    @GetMapping("/{programId}")
    public ApiResponse<SuccessBody<QueryProgramResponse>> findOne(
            @PathVariable("programId") Long programId){
        QueryProgramResponse response = getProgramUsecase.getProgram(programId);
        return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.GET);
    }
}
