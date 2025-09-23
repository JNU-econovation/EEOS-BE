package com.blackcompany.eeos.target.persistence.policy;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import com.blackcompany.eeos.target.application.model.SignType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = AttendWeightPolicyEntity.ENTITY_PREFIX)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE weight_policy SET is_deleted=true where weight_policy_id=?")
@Where(clause = "is_deleted=false")
public class AttendWeightPolicyEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "weight_policy";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_sign_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private SignType signType;

	@Column(name = ENTITY_PREFIX + "_attend_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private AttendStatus type;

	@Column(name = ENTITY_PREFIX + "_value", nullable = false)
	private int score;
}
