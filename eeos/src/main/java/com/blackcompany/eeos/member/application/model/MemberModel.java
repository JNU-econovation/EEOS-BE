package com.blackcompany.eeos.member.application.model;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.common.application.model.MemberIdModel;
import com.blackcompany.eeos.common.support.AbstractModel;
import com.blackcompany.eeos.member.application.exception.DeniedUpdateActiveException;
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
public class MemberModel implements AbstractModel, MemberIdModel {
	private Long id;
	private String name;
	private ActiveStatus activeStatus;
	private boolean isAdmin;
	private OauthServerType oauthServerType;

	public MemberModel updateActiveStatus(String status) {
		ActiveStatus requestStatus = ActiveStatus.find(status);
		canEdit(requestStatus);
		this.activeStatus = requestStatus;
		return this;
	}

	public boolean validateSame(Long memberId) {
		return id.equals(memberId);
	}

	public String getActiveStatus() {
		return activeStatus.getStatus();
	}

	private void canEdit(ActiveStatus requestStatus) {
		if (requestStatus.isAll()) {
			throw new DeniedUpdateActiveException(requestStatus.getStatus());
		}
	}

	@Override
	public Long getMemberId() {
		return id;
	}
}
