package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;
import java.util.Set;

public interface GetAttendWeightPolicyUsecase {
	AttendWeightPolicyApplicationDto getWeightPolicies(Set<String> attendStatuses);
}
