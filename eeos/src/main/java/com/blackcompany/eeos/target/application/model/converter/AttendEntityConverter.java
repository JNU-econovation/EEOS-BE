package com.blackcompany.eeos.target.application.model.converter;

import com.blackcompany.eeos.common.support.converter.AbstractEntityConverter;
import com.blackcompany.eeos.target.application.model.AttendModel;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.persistence.AttendEntity;
import org.springframework.stereotype.Component;

@Component
public class AttendEntityConverter implements AbstractEntityConverter<AttendEntity, AttendModel> {

	@Override
	public AttendModel from(AttendEntity source) {
		return AttendModel.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.programId(source.getProgramId())
				.status(source.getStatus())
				.build();
	}

	@Override
	public AttendEntity toEntity(AttendModel source) {
		return AttendEntity.builder()
				.id(source.getId())
				.memberId(source.getMemberId())
				.programId(source.getProgramId())
				.status(AttendStatus.find(source.getStatus()))
				.build();
	}

	public AttendEntity toEntity(Long memberId, Long programId) {
		return AttendEntity.builder()
				.memberId(memberId)
				.programId(programId)
				.status(AttendStatus.NONRESPONSE)
				.build();
	}
}
