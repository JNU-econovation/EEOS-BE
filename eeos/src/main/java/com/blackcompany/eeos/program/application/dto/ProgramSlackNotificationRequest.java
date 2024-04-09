package com.blackcompany.eeos.program.application.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramSlackNotificationRequest implements SlackNotificationRequest {

	private @NotNull String programUrl;
}
