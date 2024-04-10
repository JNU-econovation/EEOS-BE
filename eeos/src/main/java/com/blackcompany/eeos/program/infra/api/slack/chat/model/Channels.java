package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/env.properties")
@Configuration
@Getter
public class Channels {
	@Value("${slack.channel.econovation.notification}")
	private String ECONOVATION_NOTIFICATION;

	@Value("${slack.channel.econovation.small_talk}")
	private String ECONOVATION_SMALL_TALK;

	@Value("${slack.channel.black-company.slack-message-test}")
	private String BLACK_COMPANY_TEST;
}
