package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.program.application.dto.ProgramSlackNotificationRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProgramNotificationModel extends ProgramModel{

    private String url;

    public ProgramNotificationModel(ProgramModel model, String url){
        super(model);
        this.url = url;
    }

    public static ProgramNotificationModel of(ProgramModel model, ProgramSlackNotificationRequest request) {
        return new ProgramNotificationModel(model, request.getProgramUrl());
    }
}