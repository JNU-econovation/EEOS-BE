package com.blackcompany.eeos.program.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateProgramRequest implements CommandProgramRequest {

	private @NotNull String title;
	private @NotNull Timestamp deadLine;
	private @NotNull String content;
	private @NotNull String category;
	private @NotNull String type;
	private @NotNull List<Team> teams;
	private @NotNull String programGithubUrl;
	private List<ProgramMembers> members;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	static class Team {
		private Long teamId;
	}

	@JsonIgnore
	public List<Long> getTeamIds() {
		return this.teams.stream().map(t -> t.getTeamId()).collect(Collectors.toList());
	}
}
