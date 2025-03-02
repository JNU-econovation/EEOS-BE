package com.blackcompany.eeos.target.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractApplicationDto;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.SignType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AttendWeightPolicyApplicationDto implements AbstractApplicationDto {
	private List<WeightPolicyDto> policies;

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class WeightPolicyDto {
		private AttendStatus type;
		private SignType signType;
		private int score;
	}
}
