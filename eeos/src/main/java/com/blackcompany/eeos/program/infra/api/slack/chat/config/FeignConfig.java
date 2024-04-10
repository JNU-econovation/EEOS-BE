package com.blackcompany.eeos.program.infra.api.slack.chat.config;

import com.blackcompany.eeos.program.infra.api.slack.chat.interceptor.SlackChatOpenFeignInterceptor;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.BotTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final BotTokens tokens;
    @Bean
    public SlackChatOpenFeignInterceptor feignInterceptor(){
        return new SlackChatOpenFeignInterceptor(tokens);
    }

}
