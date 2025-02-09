package com.blackcompany.eeos.penalty.application.dto;

import com.blackcompany.eeos.common.support.dto.AbstractApplicationDto;
import com.blackcompany.eeos.penalty.application.model.SignType;
import com.blackcompany.eeos.penalty.application.model.WeightType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeightPolicyApplicationDto implements AbstractApplicationDto {
	private List<Policy> policies;

	@Getter
	@AllArgsConstructor
	public static class Policy {
		private WeightType type;
		private SignType sign;
		private int point;
	}
}
