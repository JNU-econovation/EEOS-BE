package com.blackcompany.eeos.program.application.usecase;

public interface DeleteProgramUsecase {
	/**
	 * 프로그램을 삭제한다.
	 *
	 * <p>해당 요청 시 프로그램과 관련있는 사람들의 정보도 삭제된다.
	 *
	 * @param memberId 삭제를 요청한 사람의 정보
	 * @param programId 삭제 대상 프로그램 정보
	 */
	void delete(Long memberId, Long programId);
}
