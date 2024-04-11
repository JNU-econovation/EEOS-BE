package com.blackcompany.eeos.program.infra.api.slack.chat.client;

import com.blackcompany.eeos.config.ChatOpenFeignConfig;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SlackChatOpenFeign", url = "https://slack.com/api", configuration = ChatOpenFeignConfig.class)
public interface SlackChatApiClientImpl extends SlackChatApiClient {

	@PostMapping(path = "/chat.postMessage", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	SlackChatPostMessageResponse post(@RequestParam("channel") final String channel,
									  @RequestParam("blocks") final String blocks,
									  @RequestParam("username") final String username);
}
