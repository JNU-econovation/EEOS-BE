package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import com.blackcompany.eeos.common.support.AbstractModel;

public interface MessageModel<T> extends AbstractModel {
	<T> T getMessage();
}
