package com.blackcompany.eeos.program.infra.api.slack.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramSlackNotificationRequest implements SlackNotificationRequest{

    private @NotNull String programUrl;

}
