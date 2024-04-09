package com.blackcompany.eeos.program.infra.api.slack.chat.client;


import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "SlackChatOpenFeign", url="https://slack.com/api")
public interface SlackChatApiClientImpl extends SlackChatApiClient{

    @PostMapping(path="/chat.postMessage")
    SlackChatPostMessageResponse post(
            @RequestBody ChatPostModel model);

}
