package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.AttendWeightPolicyModel;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface AttendWeightPolicyRepository {
	List<AttendWeightPolicyModel> saveAll(List<AttendWeightPolicyModel> weightPolicies);

	void deleteByAttendTypes(Collection<AttendStatus> attendTypes);

	List<AttendWeightPolicyModel> findLatestAttendWeight(Set<AttendStatus> attendStatuses);
}
