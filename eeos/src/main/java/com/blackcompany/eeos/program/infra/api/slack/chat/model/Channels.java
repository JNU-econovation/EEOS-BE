package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:/env.properties")
@Component
@Getter
public class Channels {
    @Value("${slack.channel.econovation.notification}")
    private String ECONOVATION_NOTIFICATION;

    @Value("${slack.channel.black-company.eeos-test}")
    private String BLACK_COMPANY_TEST;
}
