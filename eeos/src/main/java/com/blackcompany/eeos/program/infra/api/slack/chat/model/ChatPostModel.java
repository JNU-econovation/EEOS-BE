package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel.Block;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
public class ChatPostModel implements MessageModel<Block> {

	private String channel;
	private Block[] message;

	@JsonProperty("username")
	private String userName;

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Block implements BlockModel {
		private String type;
		private TextModel text;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Text implements TextModel {
		private String type;
		private String text;
	}
}
