package com.blackcompany.eeos.target.application.usecase;

import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;

public interface CommandAttendWeightPolicyUsecase {
	void changeWeightPolicy(AttendWeightPolicyApplicationDto command);
}
