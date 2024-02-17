package com.blackcompany.eeos.target.application.model;

import com.blackcompany.eeos.common.application.model.MemberIdModel;
import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TeamBuildingTargetModel implements AbstractModel, MemberIdModel {
	private Long id;
	private Long memberId;
	private Long teamBuildingId;
	private String content;

	@Override
	public Long getMemberId() {
		return memberId;
	}
}
