package com.blackcompany.eeos.target.application.dto.converter;

import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.target.application.dto.AttendInfoActiveStatusResponse;
import org.springframework.stereotype.Component;

@Component
public class AttendInfoActiveStatusConverter {
	public AttendInfoActiveStatusResponse from(
			MemberModel source, String attendStatus, String activeStatus) {
		return AttendInfoActiveStatusResponse.builder()
				.memberId(source.getId())
				.name(source.getName())
				.activeStatus(activeStatus)
				.attendStatus(attendStatus)
				.build();
	}
}
