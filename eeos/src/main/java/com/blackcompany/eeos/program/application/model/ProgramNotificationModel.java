package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.program.infra.api.slack.chat.dto.ProgramSlackNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProgramNotificationModel{

    private ProgramModel programModel;
    private String programUrl;

    public static ProgramNotificationModel from(ProgramModel model, ProgramSlackNotificationRequest request){
        return ProgramNotificationModel.builder()
                .programModel(model)
                .programUrl(request.getProgramUrl())
                .build();
    }

    public String getTitle(){
        return programModel
                .getTitle();
    }

    public String getContent(){
        return programModel
                .getContent();
    }

    public String getProgramUrl(){
        return this.programUrl;
    }

    public String getProgramDate(){
        return programModel
                .getProgramDate()
                .toLocalDateTime()
                .toString();
    }
}