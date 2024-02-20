package com.blackcompany.eeos.teamBuilding.infra.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ParticipantRequest {
	private List<EachParticipantResponse> participantResponses;
	private int maxTeamSize;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder(toBuilder = true)
	public static class EachParticipantResponse {
		private String memberId;
		private String sentence;
	}
}
