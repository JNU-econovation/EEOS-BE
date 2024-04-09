package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:/env.properties")
@Getter
@Component
public class BotTokens {
	@Value("${slack.bot.black-company.eeos-test")
	private String BLACK_COMPANY_EEOS_BOT;

	private String ECONOVATION_EEOS_BOT = "";
}
