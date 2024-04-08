package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel.Block;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ChatPostModel implements MessageModel<Block>{

    private String token;
    private String channel;
    private Block[] message;

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class Block implements BlockModel{
        private String type;
        private TextModel text;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class Text implements TextModel{
        private String type;
        private String text;
    }
}
