package com.blackcompany.eeos.target.application.dto.converter;

import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.target.application.dto.AttendInfoResponse;
import org.springframework.stereotype.Component;

@Component
public class AttendInfoConverter {

	public AttendInfoResponse from(final MemberModel source, final String attendStatus) {
		return AttendInfoResponse.builder()
				.memberId(source.getId())
				.name(source.getName())
				.attendStatus(attendStatus)
				.build();
	}

	public AttendInfoResponse from(final MemberEntity source, final String status) {
		return AttendInfoResponse.builder()
				.memberId(source.getId())
				.name(source.getName())
				.attendStatus(status)
				.build();
	}
}
