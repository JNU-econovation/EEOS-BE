package com.blackcompany.eeos.teamBuilding.application.dto;

import com.blackcompany.eeos.attend.application.dto.TargetMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TeamBuildingMember implements TargetMember {
	private Long memberId;
}
