package com.blackcompany.eeos.teamBuilding.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.teamBuilding.application.model.RestrictTeamBuildingModel;
import com.blackcompany.eeos.teamBuilding.persistence.RestrictTeamBuildingEntity;
import org.springframework.stereotype.Component;

@Component
public class RestrictTeamBuildingConverter
		implements AbstractEntityConverter<RestrictTeamBuildingEntity, RestrictTeamBuildingModel> {
	@Override
	public RestrictTeamBuildingModel from(RestrictTeamBuildingEntity source) {
		return RestrictTeamBuildingModel.builder()
				.id(source.getId())
				.totalActiveCount(source.getTotalActiveCount())
				.version(source.getVersion())
				.build();
	}

	@Override
	public RestrictTeamBuildingEntity toEntity(RestrictTeamBuildingModel source) {
		return RestrictTeamBuildingEntity.builder()
				.id(source.getId())
				.totalActiveCount(source.getTotalActiveCount())
				.version(source.getVersion())
				.build();
	}
}
