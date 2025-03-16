package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractResponseDto;

public record AttendPenaltyRankingResponse(
        boolean isRanked,
        int ranking)
implements AbstractResponseDto {

    public static AttendPenaltyRankingResponse empty(){
        return new AttendPenaltyRankingResponse(false, 0);
    }

}
