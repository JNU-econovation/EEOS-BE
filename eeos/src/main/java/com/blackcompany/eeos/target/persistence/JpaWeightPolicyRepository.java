package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.target.application.model.AttendStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWeightPolicyRepository extends JpaRepository<AttendWeightPolicyEntity, Long> {
	void deleteByTypeIn(Collection<AttendStatus> weightTypes);

	List<AttendWeightPolicyEntity> findByTypeIn(Collection<AttendStatus> weightTypes);
}
