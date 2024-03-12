package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.dto.TargetMember;
import java.util.List;

public interface CommandTargetMemberService {

	/** 관련 대상자를 선택한다. */
	<T extends TargetMember> void save(final Long eventId, final List<T> members);
}
