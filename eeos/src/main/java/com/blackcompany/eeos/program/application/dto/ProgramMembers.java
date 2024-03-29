package com.blackcompany.eeos.program.application.dto;

import com.blackcompany.eeos.target.application.dto.TargetMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProgramMembers implements TargetMember {
	private Long memberId;
}
