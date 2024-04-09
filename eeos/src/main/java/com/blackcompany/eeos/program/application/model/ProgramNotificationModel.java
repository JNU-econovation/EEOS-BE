package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.program.application.dto.ProgramSlackNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    public void validateNotify(long memberId){
        programModel.validateNotify(memberId);
    }
}