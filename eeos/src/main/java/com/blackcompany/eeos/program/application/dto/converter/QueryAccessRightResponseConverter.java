package com.blackcompany.eeos.program.application.dto.converter;

import com.blackcompany.eeos.program.application.dto.QueryAccessRightResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryAccessRightResponseConverter {
	public QueryAccessRightResponse to(String accessRight) {
		return QueryAccessRightResponse.builder().accessRight(accessRight).build();
	}
}
