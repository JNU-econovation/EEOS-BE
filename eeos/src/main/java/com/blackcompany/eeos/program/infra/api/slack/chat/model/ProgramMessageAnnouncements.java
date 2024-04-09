package com.blackcompany.eeos.program.infra.api.slack.chat.model;

import lombok.Getter;

@Getter
public enum ProgramMessageAnnouncements {

    HEADER_TOP("@channel \n*[EEOS 행사 생성 알림]*"),
    HEADER_MID("🎉EEOS 행사가 생성되었습니다!🎉"),
    PROGRAM_NAME("1. 행사 제목\n"),
    PROGRAM_DATE("2. 행사 일정\n"),
    PROGRAM_URL("3. 행사 링크\n"),
    BOTTOM("행사에 관한 세부 내용과 출석 여부는 EEOS를 이용해주세요!");

    private String announcement;

    ProgramMessageAnnouncements(String announcement){ this.announcement = announcement; }

}
