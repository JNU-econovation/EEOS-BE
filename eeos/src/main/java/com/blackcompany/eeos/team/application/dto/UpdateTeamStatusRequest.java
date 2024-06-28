package com.blackcompany.eeos.team.application.dto;

import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateTeamStatusRequest {
	private @NotNull String teamId;
	private @NotNull Boolean teamStatus;
}
