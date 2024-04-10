package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:/env.properties")
@Getter
@Configuration
public class BotTokens {

	@Value("${slack.bot.black-company.eeos}")
	private String BLACK_COMPANY_EEOS_BOT;

	@Value("${slack.bot.econovation.eeos}")
	private String ECONOVATION_EEOS_BOT;
}
