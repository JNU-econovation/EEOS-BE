package com.blackcompany.eeos.attend.application.dto.converter;

import com.blackcompany.eeos.attend.application.dto.AttendInfoResponse;
import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import org.springframework.stereotype.Component;

@Component
public class AttendInfoConverter {

	public AttendInfoResponse from(final MemberEntity source, final AttendStatus attendStatus) {
		return AttendInfoResponse.builder()
				.memberId(source.getId())
				.generation(source.getGeneration())
				.name(source.getName())
				.attendStatus(attendStatus.getStatus())
				.build();
	}

	public AttendInfoResponse from(final MemberEntity source, final String status) {
		return AttendInfoResponse.builder()
				.memberId(source.getId())
				.generation(source.getGeneration())
				.name(source.getName())
				.attendStatus(status)
				.build();
	}
}
