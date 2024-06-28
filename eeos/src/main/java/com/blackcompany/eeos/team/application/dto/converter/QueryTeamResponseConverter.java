package com.blackcompany.eeos.team.application.dto.converter;

import com.blackcompany.eeos.team.application.dto.QueryTeamResponse;
import com.blackcompany.eeos.team.application.dto.QueryTeamsResponse;
import com.blackcompany.eeos.team.application.model.TeamModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class QueryTeamResponseConverter {
	public QueryTeamResponse from(TeamModel source) {
		return QueryTeamResponse.builder()
				.teamId(source.getId())
				.teamName(source.getName())
				.teamStatus(source.isStatus())
				.build();
	}

	public QueryTeamsResponse from(List<TeamModel> sources) {
		return QueryTeamsResponse.builder()
				.teams(sources.stream().map(this::from).collect(Collectors.toList()))
				.build();
	}
}
