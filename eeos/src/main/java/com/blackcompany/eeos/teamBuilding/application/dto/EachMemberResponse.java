package com.blackcompany.eeos.teamBuilding.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class EachMemberResponse {
	private Long memberId;
	private String name;
}
