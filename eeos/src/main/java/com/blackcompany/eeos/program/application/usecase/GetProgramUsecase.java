package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.QueryProgramResponse;

public interface GetProgramUsecase {
	/**
	 * 프로그램 detail 정보를 가져온다.
	 *
	 * <p>이때 프로그램 접근 권한도 같이 반환한다.
	 *
	 * @param memberId 접근한 유저의 정보
	 * @param programId 프로그램 정보
	 * @return
	 */
	QueryProgramResponse getProgram(Long memberId, Long programId);
}
