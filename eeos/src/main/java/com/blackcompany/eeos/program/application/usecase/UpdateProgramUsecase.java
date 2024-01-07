package com.blackcompany.eeos.program.application.usecase;

import com.blackcompany.eeos.program.application.dto.CommandProgramResponse;
import com.blackcompany.eeos.program.application.dto.UpdateProgramRequest;

public interface UpdateProgramUsecase {
	/**
	 * 프로그램을 수정한다.
	 *
	 * <p>해당 요청으로 프로그램 참석 대상자 수정도 진행한다.
	 *
	 * @param request memberId, 프로그램 수정을 요청하는 사람
	 * @param request programId, 프로그램 수정 하기 위한 request 객체
	 * @return 프로그램 식별 id 전달
	 */
	CommandProgramResponse update(Long memberId, Long programId, UpdateProgramRequest request);
}
