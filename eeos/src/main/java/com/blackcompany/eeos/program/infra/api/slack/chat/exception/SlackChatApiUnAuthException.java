package com.blackcompany.eeos.program.infra.api.slack.chat.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SlackChatApiUnAuthException extends BusinessException {

    private static final String FAIL_CODE = "1009";
    private final String message;

    public SlackChatApiUnAuthException(String message){
        super(FAIL_CODE, HttpStatus.UNAUTHORIZED);
        this.message = message;
    }

    @Override
    public String getMessage(){ return message; }

}
