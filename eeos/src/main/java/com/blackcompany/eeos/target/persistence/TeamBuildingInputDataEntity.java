package com.blackcompany.eeos.target.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Builder
public class TeamBuildingInputDataEntity {
	public static final String ENTITY_PREFIX = "team_building_input";

	@Column(name = ENTITY_PREFIX + "_content")
	private String content;

	@Column(name = ENTITY_PREFIX + "_status", nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private TeamBuildingInputStatus inputStatus = TeamBuildingInputStatus.INCOMPLETE;

	public static TeamBuildingInputDataEntity init() {
		return TeamBuildingInputDataEntity.builder()
				.inputStatus(TeamBuildingInputStatus.INCOMPLETE)
				.build();
	}
}
