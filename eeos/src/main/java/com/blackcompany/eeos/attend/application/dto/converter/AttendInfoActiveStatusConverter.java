package com.blackcompany.eeos.attend.application.dto.converter;

import com.blackcompany.eeos.attend.application.dto.AttendInfoActiveStatusResponse;
import com.blackcompany.eeos.member.application.model.MemberModel;
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
