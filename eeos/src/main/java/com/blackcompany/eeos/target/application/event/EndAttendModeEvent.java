package com.blackcompany.eeos.target.application.event;

import java.util.Set;
import lombok.Getter;

@Getter
public class EndAttendModeEvent {

	private final Set<Long> programIds;

	private EndAttendModeEvent(Set<Long> programIds) {
		this.programIds = programIds;
	}

	public static EndAttendModeEvent of(Set<Long> programIds) {
		return new EndAttendModeEvent(programIds);
	}
}
