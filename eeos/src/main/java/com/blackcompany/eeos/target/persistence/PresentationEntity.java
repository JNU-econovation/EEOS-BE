package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
@Table(name = PresentationEntity.ENTITY_PREFIX)
@SQLDelete(sql = "UPDATE presentation SET is_deleted=true WHERE presentation_id=?")
@Where(clause = "is_deleted=false")
public class PresentationEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "presentation";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_program_id", nullable = false)
	private Long programId;

	@Column(name = ENTITY_PREFIX + "_team_id", nullable = false)
	private Long teamId;
}
