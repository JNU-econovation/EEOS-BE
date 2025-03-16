package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.dto.AttendWeightPolicyApplicationDto;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.AttendWeightPolicyModel;
import com.blackcompany.eeos.target.application.model.SignType;
import com.blackcompany.eeos.target.application.usecase.CommandAttendWeightPolicyUsecase;
import com.blackcompany.eeos.target.application.usecase.GetAttendWeightPolicyUsecase;
import com.blackcompany.eeos.target.persistence.AttendWeightPolicyRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AttendWeightPolicyService
		implements CommandAttendWeightPolicyUsecase, GetAttendWeightPolicyUsecase {
	private final AttendWeightPolicyRepository repository;

	@Override
	@Transactional
	public void changeWeightPolicy(AttendWeightPolicyApplicationDto command) {
		Set<AttendStatus> attendStatuses =
				command.getPolicies().stream()
						.map(AttendWeightPolicyApplicationDto.WeightPolicyDto::getType)
						.collect(Collectors.toSet());

		repository.deleteByAttendTypes(attendStatuses);

		List<AttendWeightPolicyModel> models =
				command.getPolicies().stream()
						.map(
								policy ->
										AttendWeightPolicyModel.builder()
												.type(policy.getType())
												.signType(policy.getSignType())
												.score(policy.getScore())
												.build())
						.toList();

		repository.saveAll(models);
	}

	@Override
	public AttendWeightPolicyApplicationDto getWeightPolicies(Set<String> attendStatuses) {
		Set<AttendStatus> statuses =
				attendStatuses.stream().map(AttendStatus::find).collect(Collectors.toSet());
		List<AttendWeightPolicyModel> existingPolicies = repository.findLatestAttendWeight(statuses);

		Set<AttendStatus> existingTypes =
				existingPolicies.stream().map(AttendWeightPolicyModel::getType).collect(Collectors.toSet());

		List<AttendWeightPolicyModel> allPolicies =
				Stream.concat(
								existingPolicies.stream(),
								statuses.stream()
										.filter(status -> !existingTypes.contains(status))
										.map(
												status ->
														AttendWeightPolicyModel.builder()
																.type(status)
																.signType(SignType.PLUS)
																.score(0)
																.build()))
						.toList();

		return AttendWeightPolicyApplicationDto.builder()
				.policies(
						allPolicies.stream()
								.map(
										policy ->
												AttendWeightPolicyApplicationDto.WeightPolicyDto.builder()
														.type(policy.getType())
														.signType(policy.getSignType())
														.score(policy.getScore())
														.build())
								.toList())
				.build();
	}
}
