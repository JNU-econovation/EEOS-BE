package com.blackcompany.eeos.target.presentation.dto;

import com.blackcompany.eeos.common.support.dto.AbstractWebDto;
import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.SignType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendWeightPolicyWebDto implements AbstractWebDto<AttendWeightPolicyApplicationDto> {
	private List<WeightPolicyDto> policies;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class WeightPolicyDto {
		private String type;
		private String signType;
		private int score;
	}

	@Override
	public AttendWeightPolicyApplicationDto toApplicationRequest() {
		return AttendWeightPolicyApplicationDto.builder()
				.policies(
						policies.stream()
								.map(
										policy ->
												AttendWeightPolicyApplicationDto.WeightPolicyDto.builder()
														.signType(SignType.find(policy.signType))
														.type(AttendStatus.find(policy.type))
														.score(policy.score)
														.build())
								.toList())
				.build();
	}
}
