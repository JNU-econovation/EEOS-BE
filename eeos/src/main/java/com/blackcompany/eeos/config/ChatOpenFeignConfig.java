package com.blackcompany.eeos.config;

import com.blackcompany.eeos.program.infra.api.slack.chat.interceptor.SlackBCChatOpenFeignInterceptor;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.BotTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class ChatOpenFeignConfig {

    private final BotTokens tokens;
    @Bean
    public SlackBCChatOpenFeignInterceptor feignInterceptor(){
        return new SlackBCChatOpenFeignInterceptor(tokens);
    }

}
