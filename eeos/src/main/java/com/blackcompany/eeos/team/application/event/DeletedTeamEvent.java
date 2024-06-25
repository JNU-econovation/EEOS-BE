package com.blackcompany.eeos.team.application.event;

public class DeletedTeamEvent {
	private final Long teamId;

	private DeletedTeamEvent(Long teamId) {
		this.teamId = teamId;
	}

	public static DeletedTeamEvent of(Long teamId) {
		return new DeletedTeamEvent(teamId);
	}
}
