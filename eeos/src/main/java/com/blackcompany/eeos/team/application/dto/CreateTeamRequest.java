package com.blackcompany.eeos.team.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateTeamRequest implements AbstractRequestDto {
	private @NotNull String teamName;
}
