package com.blackcompany.eeos.program.application.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.blackcompany.eeos.program.application.dto.CreateProgramRequest.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateProgramRequest implements CommandProgramRequest {

	private @NotNull String title;
	private @NotNull Timestamp deadLine;
	private @NotNull String content;
	private @NotNull String category;
	private @NotNull String type;
	private @NotNull List<Team> teams;
	private @NotNull String programGithubUrl;
	private List<ChangeAllAttendStatusRequest> members;

	public List<Long> getTeamIds(){
		return this.teams.stream()
				.map(t -> t.getTeamId())
				.collect(Collectors.toList());
	}
}
