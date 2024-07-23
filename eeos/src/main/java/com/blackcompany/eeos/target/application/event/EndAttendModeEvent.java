package com.blackcompany.eeos.target.application.event;

import lombok.Getter;

@Getter
public class EndAttendModeEvent {

	private final Long programId;

	private EndAttendModeEvent(Long programId) {
		this.programId = programId;
	}

	public static EndAttendModeEvent of(Long programId) {
		return new EndAttendModeEvent(programId);
	}
}
