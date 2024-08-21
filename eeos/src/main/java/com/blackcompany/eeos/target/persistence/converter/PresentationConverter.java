package com.blackcompany.eeos.target.persistence.converter;

import com.blackcompany.eeos.target.persistence.PresentationEntity;
import org.springframework.stereotype.Component;

@Component
public class PresentationConverter {

	public PresentationEntity from(Long programId, Long teamId) {
		return PresentationEntity.builder().teamId(teamId).programId(programId).build();
	}
}
