package com.blackcompany.eeos.target.persistence.policy;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.AttendWeightPolicyModel;
import com.blackcompany.eeos.target.application.repository.AttendWeightPolicyRepository;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
class AttendWeightPolicyRepositoryImpl implements AttendWeightPolicyRepository {
	private final JpaWeightPolicyRepository jpaRepository;

	@Override
	public List<AttendWeightPolicyModel> saveAll(List<AttendWeightPolicyModel> weightPolicies) {
		return jpaRepository
				.saveAll(weightPolicies.stream().map(this::convertToEntity).toList())
				.stream()
				.map(this::convertToDomain)
				.toList();
	}

	@Override
	public void deleteByAttendTypes(Collection<AttendStatus> attendTypes) {
		jpaRepository.deleteByTypeIn(attendTypes);
	}

	@Override
	public List<AttendWeightPolicyModel> findLatestAttendWeight(Set<AttendStatus> attendStatuses) {
		return jpaRepository.findByTypeIn(attendStatuses).stream()
				.map(this::convertToDomain)
				.toList();
	}

	private AttendWeightPolicyModel convertToDomain(AttendWeightPolicyEntity entity) {
		return AttendWeightPolicyModel.builder()
				.id(entity.getId())
				.signType(entity.getSignType())
				.type(entity.getType())
				.score(entity.getScore())
				.build();
	}

	private AttendWeightPolicyEntity convertToEntity(AttendWeightPolicyModel model) {
		return AttendWeightPolicyEntity.builder()
				.id(model.getId())
				.signType(model.getSignType())
				.type(model.getType())
				.score(model.getScore())
				.build();
	}
}
