package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.AttendWeightPolicyModel;
import com.blackcompany.eeos.target.application.model.SignType;
import com.blackcompany.eeos.target.application.repository.AttendWeightPolicyRepository;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendWeightCalculator {

	private final AttendWeightPolicyRepository repository;

	/** 모든 출석 상태에 대한 정책 목록 조회 (기본값 포함) */
	public List<AttendWeightPolicyModel> getAllPoliciesWithDefaults() {
		return getPoliciesWithDefaults(EnumSet.allOf(AttendStatus.class));
	}

	/** 특정 상태들에 대한 정책 목록 조회 (기본값 포함) */
	public List<AttendWeightPolicyModel> getPoliciesWithDefaults(Set<AttendStatus> statuses) {
		List<AttendWeightPolicyModel> existingPolicies = repository.findLatestAttendWeight(statuses);

		Map<AttendStatus, AttendWeightPolicyModel> policyMap =
				existingPolicies.stream()
						.collect(Collectors.toMap(AttendWeightPolicyModel::getType, Function.identity()));

		List<AttendWeightPolicyModel> resultPolicies =
				new ArrayList<>(existingPolicies.size() + statuses.size());

		for (AttendStatus status : statuses) {
			resultPolicies.add(policyMap.getOrDefault(status, createDefaultPolicy(status)));
		}

		return resultPolicies;
	}

	/** 상태별 가중치 점수 맵 생성 */
	public Map<AttendStatus, Integer> getWeightScoreMap(Set<AttendStatus> statuses) {
		return getPoliciesWithDefaults(statuses).stream()
				.collect(Collectors.toMap(AttendWeightPolicyModel::getType, this::calculateActualScore));
	}

	/** 여러 상태의 총 점수 계산 */
	public int calculateTotalScore(List<AttendStatus> statuses) {
		if (statuses.isEmpty()) {
			return 0;
		}

		Map<AttendStatus, Integer> scoreMap = getWeightScoreMap(Set.copyOf(statuses));

		return statuses.stream().mapToInt(status -> scoreMap.getOrDefault(status, 0)).sum();
	}

	/** 기본 정책 생성 */
	private AttendWeightPolicyModel createDefaultPolicy(AttendStatus status) {
		return AttendWeightPolicyModel.builder().type(status).signType(SignType.PLUS).score(0).build();
	}

	/** 부호를 고려한 실제 점수 계산 */
	private int calculateActualScore(AttendWeightPolicyModel policy) {
		return policy.getSignType() == SignType.PLUS ? policy.getScore() : -policy.getScore();
	}
}
