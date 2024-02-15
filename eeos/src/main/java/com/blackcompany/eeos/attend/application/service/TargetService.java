package com.blackcompany.eeos.attend.application.service;

import com.blackcompany.eeos.attend.application.dto.TargetMember;
import java.util.List;

public interface TargetService {

	/** 관련 대상자를 선택한다. */
	<T extends TargetMember> void save(final Long eventId, final List<T> members);
}
