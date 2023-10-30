package com.blackcompany.eeos.attend.application.dto.converter;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import org.springframework.stereotype.Component;

@Component
public class AttendInfoConverter {
	public AttendInfoResponse from(final MemberModel target, final AttendStatus attendStatus) {
		return AttendInfoResponse.builder()
				.id(target.getId())
				.generation(target.getGeneration())
				.name(target.getName())
				.attendStatus(attendStatus.getStatus())
				.build();
	}
}
