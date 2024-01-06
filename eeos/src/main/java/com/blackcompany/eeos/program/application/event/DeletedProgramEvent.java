package com.blackcompany.eeos.program.application.event;

import lombok.Getter;

@Getter
public class DeletedProgramEvent {
	private final Long programId;

	private DeletedProgramEvent(Long programId) {
		this.programId = programId;
	}

	/**
	 * 프로그램 삭제 이벤트를 생성한다.
	 *
	 * <p>이벤트 생성에서 예외가 발생하면 서비스 로직에 영향을 주므로 이벤트 리스너에서 핸들링을 할 때 예외 처리를 한다.
	 *
	 * @param programId 삭제된 프로그림 정보
	 * @return
	 */
	public static DeletedProgramEvent of(Long programId) {
		return new DeletedProgramEvent(programId);
	}
}
