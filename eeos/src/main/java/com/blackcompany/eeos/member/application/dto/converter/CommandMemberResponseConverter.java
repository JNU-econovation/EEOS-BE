package com.blackcompany.eeos.member.application.dto.converter;

import com.blackcompany.eeos.member.application.dto.CommandMemberResponse;
import org.springframework.stereotype.Component;

@Component
public class CommandMemberResponseConverter {
	public CommandMemberResponse from(String name, String status) {
		return CommandMemberResponse.builder().name(name).activeStatus(status).build();
	}
}
