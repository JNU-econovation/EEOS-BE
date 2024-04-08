package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import com.blackcompany.eeos.common.support.AbstractModel;

public interface TextModel extends AbstractModel {

    String getType();

    String getText();
}
