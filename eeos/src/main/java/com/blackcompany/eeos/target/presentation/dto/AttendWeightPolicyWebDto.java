package com.blackcompany.eeos.target.presentation.dto;

import com.blackcompany.eeos.common.support.dto.AbstractWebDto;
import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.SignType;
import jakarta.validation.constraints.NotEmpty;
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
	@NotEmpty(message = "정책 목록은 비어있을 수 없습니다")
	private List<WeightPolicyDto> policies;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	public static class WeightPolicyDto {
		@NotEmpty(message = "타입은 필수 항목입니다")
		private String type;

		@NotEmpty(message = "부호 타입은 필수 항목입니다")
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
