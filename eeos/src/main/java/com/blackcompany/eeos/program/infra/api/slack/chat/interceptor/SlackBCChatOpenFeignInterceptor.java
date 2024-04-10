package com.blackcompany.eeos.program.infra.api.slack.chat.interceptor;

import com.blackcompany.eeos.program.infra.api.slack.chat.model.BotTokens;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
@Slf4j
public class SlackBCChatOpenFeignInterceptor implements RequestInterceptor {

    private final BotTokens tokens;

    @Override
    public void apply(RequestTemplate template){
        template.header(HttpHeaders.AUTHORIZATION, "Bearer "+tokens.getBLACK_COMPANY_EEOS_BOT());
    }

}
