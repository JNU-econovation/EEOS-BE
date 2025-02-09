package com.blackcompany.eeos.penalty.presentation.dto;

import com.blackcompany.eeos.common.support.dto.AbstractWebDto;
import com.blackcompany.eeos.penalty.application.dto.WeightPolicyApplicationDto;
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
public class WeightPolicyWebDto implements AbstractWebDto<WeightPolicyApplicationDto> {
	private List<WeightPolicyDto> policies;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class WeightPolicyDto {
		private String type;
		private String sign;
		private int point;
	}

	@Override
	public WeightPolicyApplicationDto toApplicationRequest() {
		return WeightPolicyApplicationDto.builder()
				.policies(
						policies.stream()
								.map(
										p ->
												new WeightPolicyApplicationDto.Policy(
														WeightType.valueOf(p.type), SignType.valueOf(p.sign), p.point))
								.toList())
				.build();
	}
}
