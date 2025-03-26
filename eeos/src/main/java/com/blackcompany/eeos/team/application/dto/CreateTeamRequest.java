package com.blackcompany.eeos.team.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreateTeamRequest implements AbstractRequestDto {
	private @NotNull String teamName;
}
