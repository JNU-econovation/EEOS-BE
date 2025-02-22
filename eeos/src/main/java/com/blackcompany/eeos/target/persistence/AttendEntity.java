package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.target.application.model.AttendStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity
@Table(
		name = AttendEntity.ENTITY_PREFIX,
		indexes = {
			@Index(name = "idx_attend_program", columnList = "attend_program_id"),
			@Index(name = "idx_attend_status", columnList = "attend_status")
		})
@SQLDelete(sql = "UPDATE attend SET is_deleted=true where attend_id=?")
@Where(clause = "is_deleted=false")
public class AttendEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "attend";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_program_id", nullable = false)
	private Long programId;

	@Column(name = ENTITY_PREFIX + "_member_id", nullable = false)
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(name = ENTITY_PREFIX + "_status", nullable = false)
	@Builder.Default
	private AttendStatus status = AttendStatus.NONRELATED;

	@Column(name = ENTITY_PREFIX + "_rank")
	@Builder.Default
	private Long rank = null;
	@Column(name = ENTITY_PREFIX + "_penalty_score", nullable = false)
	@Builder.Default
	private Integer penaltyScore = 0;
}
