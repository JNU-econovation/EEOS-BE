package com.blackcompany.eeos.member.application.model;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.common.support.AbstractModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MemberModel implements AbstractModel {
	private Long id;
	private String name;
	private ActiveStatus activeStatus;
	private OauthServerType oauthServerType;

	public MemberModel updateActiveStatus(String status) {
		this.activeStatus = ActiveStatus.find(status);
		return this;
	}

	public boolean validateSame(Long memberId) {
		return id.equals(memberId);
	}
}
