package com.blackcompany.eeos.teamBuilding.application.event;

import lombok.Getter;

@Getter
public class DeletedTeamBuildingEvent {
	private final Long teamBuildingId;

	private DeletedTeamBuildingEvent(Long teamBuildingId) {
		this.teamBuildingId = teamBuildingId;
	}

	/**
	 * 팀빌딩 삭제 이벤트를 생성한다.
	 *
	 * <p>이벤트 생성에서 예외가 발생하면 서비스 로직에 영향을 주므로 이벤트 리스너에서 핸들링을 할 때 예외 처리를 한다.
	 *
	 * @param teamBuildingId 삭제된 팀빌딩 정보
	 * @return
	 */
	public static DeletedTeamBuildingEvent of(Long teamBuildingId) {
		return new DeletedTeamBuildingEvent(teamBuildingId);
	}
}
