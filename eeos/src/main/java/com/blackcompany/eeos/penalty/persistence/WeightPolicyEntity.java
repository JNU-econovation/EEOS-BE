package com.blackcompany.eeos.penalty.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.penalty.application.model.SignType;
import com.blackcompany.eeos.penalty.application.model.WeightType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = WeightPolicyEntity.ENTITY_PREFIX)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder(toBuilder = true)
@SQLDelete(sql = "UPDATE weight_policy SET is_deleted=true where weight_policy_id=?")
@Where(clause = "is_deleted=false")
public class WeightPolicyEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "weight_policy";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_sign_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private SignType signType;

	@Column(name = ENTITY_PREFIX + "_weight_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private WeightType weightType;

	@Column(name = ENTITY_PREFIX + "_value", nullable = false)
	private int score;
}
