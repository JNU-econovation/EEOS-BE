package com.blackcompany.eeos.target.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;
import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto.WeightPolicyDto;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.AttendWeightPolicyModel;
import com.blackcompany.eeos.target.application.model.SignType;
import com.blackcompany.eeos.target.persistence.AttendWeightPolicyRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AttendWeightPolicyServiceTest {

	@Mock private AttendWeightPolicyRepository repository;

	@InjectMocks private AttendWeightPolicyService service;

	@Nested
	@DisplayName("참석 가중치 변경 시")
	class ChangeWeightPolicyTest {

		@Test
		@DisplayName("기존 정책을 삭제하고 새 정책을 저장한다")
		void shouldDeleteExistingAndSaveNewPolicies() {
			// given
			WeightPolicyDto attendPolicy = createPolicyDto(AttendStatus.ATTEND, SignType.PLUS, 10);
			WeightPolicyDto absentPolicy = createPolicyDto(AttendStatus.ABSENT, SignType.MINUS, 5);
			AttendWeightPolicyApplicationDto command = createPolicyCommand(attendPolicy, absentPolicy);

			// when
			service.changeWeightPolicy(command);

			// then
			verify(repository).deleteByAttendTypes(Set.of(AttendStatus.ATTEND, AttendStatus.ABSENT));
			verify(repository)
					.saveAll(
							List.of(
									createPolicyModel(AttendStatus.ATTEND, SignType.PLUS, 10),
									createPolicyModel(AttendStatus.ABSENT, SignType.MINUS, 5)));
		}
	}

	@Nested
	@DisplayName("참석 가중치 조회 시")
	class GetWeightPoliciesTest {

		@Test
		@DisplayName("존재하지 않는 정책은 기본값으로 생성된다")
		void shouldCreateDefaultPoliciesWhenNotExists() {
			// given
			AttendStatus attendStatus = AttendStatus.ATTEND;
			Set<String> attendStatuses = Set.of(attendStatus.getStatus());

			when(repository.findLatestAttendWeight(anySet())).thenReturn(Collections.emptyList());

			// when
			AttendWeightPolicyApplicationDto result = service.getWeightPolicies(attendStatuses);

			// then
			assertThat(result.getPolicies())
					.allSatisfy(
							policy -> {
								assertThat(policy.getType()).isEqualTo(attendStatus);
								assertThat(policy.getSignType()).isEqualTo(SignType.PLUS);
								assertThat(policy.getScore()).isEqualTo(0);
							});
		}

		@Test
		@DisplayName("여러 정책 타입이 주어지면 모든 타입에 대한 정책이 반환된다")
		void shouldReturnPoliciesForAllRequestedTypes() {
			// given
			AttendStatus attendStatus = AttendStatus.ATTEND;
			AttendStatus absentStatus = AttendStatus.ABSENT;
			Set<String> attendStatuses = Set.of(attendStatus.getStatus(), absentStatus.getStatus());

			List<AttendWeightPolicyModel> existingPolicies =
					List.of(createPolicyModel(attendStatus, SignType.PLUS, 10));

			when(repository.findLatestAttendWeight(anySet())).thenReturn(existingPolicies);

			// When
			var result = service.getWeightPolicies(attendStatuses);

			// Then
			Map<AttendStatus, WeightPolicyDto> policyMap =
					result.getPolicies().stream()
							.collect(Collectors.toMap(WeightPolicyDto::getType, Function.identity()));

			assertThat(policyMap).hasSize(attendStatuses.size());
			assertThat(policyMap.get(attendStatus).getSignType()).isEqualTo(SignType.PLUS);
			assertThat(policyMap.get(attendStatus).getScore()).isEqualTo(10);
			assertThat(policyMap.get(absentStatus).getSignType()).isEqualTo(SignType.PLUS);
			assertThat(policyMap.get(absentStatus).getScore()).isEqualTo(0);
		}
	}

	private WeightPolicyDto createPolicyDto(AttendStatus status, SignType signType, int score) {
		return WeightPolicyDto.builder().type(status).signType(signType).score(score).build();
	}

	private AttendWeightPolicyApplicationDto createPolicyCommand(WeightPolicyDto... policies) {
		return AttendWeightPolicyApplicationDto.builder().policies(List.of(policies)).build();
	}

	private AttendWeightPolicyModel createPolicyModel(
			AttendStatus status, SignType signType, int score) {
		return AttendWeightPolicyModel.builder().type(status).signType(signType).score(score).build();
	}
}
