package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import com.blackcompany.eeos.common.support.AbstractModel;

public interface BlockModel extends AbstractModel {
    String getType();

    TextModel getText();
    void setType(String type);

    void setText(String text);
}
