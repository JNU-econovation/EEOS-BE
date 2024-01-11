package com.blackcompany.eeos.attend.persistence;

import com.blackcompany.eeos.attend.application.model.AttendStatus;
import com.blackcompany.eeos.common.persistence.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
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
}
