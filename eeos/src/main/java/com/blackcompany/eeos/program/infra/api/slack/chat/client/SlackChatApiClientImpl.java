package com.blackcompany.eeos.program.infra.api.slack.chat.client;

import com.blackcompany.eeos.program.infra.api.slack.chat.config.FeignConfig;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.SlackChatPostMessageResponse;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel;
import com.sun.xml.bind.v2.runtime.output.Encoded;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SlackChatOpenFeign", url = "https://slack.com/api", configuration = FeignConfig.class)
public interface SlackChatApiClientImpl extends SlackChatApiClient {

	@PostMapping(path = "/chat.postMessage", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	SlackChatPostMessageResponse post(@RequestParam("channel") final String channel,
									  @RequestParam("blocks") final String blocks,
									  @RequestParam("username") final String username);
}
