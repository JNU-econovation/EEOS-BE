package com.blackcompany.eeos.program.application.dto;

import java.sql.Timestamp;
import java.util.List;
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
	private @NotNull List<Team> teamList;
	private @NotNull String programGithubUrl;
	private List<ProgramMembers> members;

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	static class Team{
		private Long teamId;
	}
}
