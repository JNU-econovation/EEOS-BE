package com.blackcompany.eeos.program.infra.api.slack.chat.exception;

import com.blackcompany.eeos.common.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class SlackChatApiUnAuthException extends BusinessException {

    private static final String FAIL_CODE = "5001";
    private final String message;

    public SlackChatApiUnAuthException(String message){
        super(FAIL_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
        this.message = "Slakc API 응답 : " + message;
    }

    @Override
    public String getMessage(){ return message; }

}
