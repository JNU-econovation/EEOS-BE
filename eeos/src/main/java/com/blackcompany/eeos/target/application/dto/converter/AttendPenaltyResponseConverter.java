package com.blackcompany.eeos.target.application.dto.converter;

import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.target.application.dto.AttendPenaltyResponse;
import com.blackcompany.eeos.target.application.model.AttendModel;
import org.springframework.stereotype.Component;

@Component
public class AttendPenaltyResponseConverter {

    public AttendPenaltyResponse from(MemberModel member, AttendModel attend, Long penaltyPoint) {
        return AttendPenaltyResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .penaltyPoint(penaltyPoint)
                .rank(attend.getRank())
                .build();
    }

}