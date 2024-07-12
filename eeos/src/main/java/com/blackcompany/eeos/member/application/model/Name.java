package com.blackcompany.eeos.member.application.model;

import lombok.Getter;

@Getter
public enum Name {

    ADMIN_NAME("00기 관리자");

    private final String name;

    Name(String name){
        this.name = name;
    }
}
