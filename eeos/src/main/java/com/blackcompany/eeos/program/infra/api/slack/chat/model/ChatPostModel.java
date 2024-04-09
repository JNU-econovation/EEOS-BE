package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.*;
import com.blackcompany.eeos.program.infra.api.slack.chat.model.ChatPostModel.Block;

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

        public void setText(String text){
            this.text.setText(text);
        }

        public void setType(String type){
            this.type = type;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Text implements TextModel{
        private String type;
        private String text;

        public void setType(String type){ this.type = type; }
        public void setText(String text){ this.text = text; }
    }
}
