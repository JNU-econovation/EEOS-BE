package com.blackcompany.eeos.target.application.dto.converter;

import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.target.application.dto.AttendPenaltyResponse;
import org.springframework.stereotype.Component;

@Component
public class AttendPenaltyResponseConverter {

    public AttendPenaltyResponse from(MemberModel member, Long penaltyPoint, Long rank) {
        return AttendPenaltyResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .penaltyPoint(penaltyPoint)
                .rank(rank)
                .build();
    }

}