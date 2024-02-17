package com.blackcompany.eeos.target.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
public class TeamBuildingInputDataEntity {
	public static final String ENTITY_PREFIX = "team_building_input";

	@Column(name = ENTITY_PREFIX + "_content")
	private String content;
}
