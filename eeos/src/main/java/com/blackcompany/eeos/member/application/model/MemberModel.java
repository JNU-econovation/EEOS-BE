package com.blackcompany.eeos.member.application.model;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.auth.application.support.MemberNameFormatter;
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
@Builder(toBuilder = true, builderMethodName = "internalBuilder")
public class MemberModel implements AbstractModel, MemberIdModel {
	private Long id;
	private String name;
	@Builder.Default private ActiveStatus activeStatus = ActiveStatus.AM;
	@Builder.Default private boolean isAdmin = false;
	private OauthServerType oauthServerType;
	@Builder.Default private Department department = Department.NONE;

	public MemberModel updateActiveStatus(String status) {
		ActiveStatus requestStatus = ActiveStatus.find(status);
		canEdit(requestStatus);
		this.activeStatus = requestStatus;
		return this;
	}

	public MemberModel updateDepartment(Department department) {
		this.department = department;
		return this;
	}

	public boolean validateSame(Long memberId) {
		return id.equals(memberId);
	}

	public String getActiveStatus() {
		return activeStatus.getStatus();
	}

	@Override
	public Long getMemberId() {
		return id;
	}

	public static MemberModelBuilder builder() {
		return internalBuilder();
	}

	public static class MemberModelBuilder {
		public MemberModelBuilder name(String name, Integer generation) {
			this.name = MemberNameFormatter.format(name, generation);
			return this;
		}

		public MemberModelBuilder name(String name) {
			this.name = name;
			return this;
		}
	}

	private void canEdit(ActiveStatus requestStatus) {
		if (requestStatus.isAll()) {
			throw new DeniedUpdateActiveException(requestStatus.getStatus());
		}
	}
}
