package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.Getter;

@Getter
public enum TextTypes {
    MARKDOWN("mrkdwn");

    private String type;

    TextTypes(String type){ this.type = type; }

}
