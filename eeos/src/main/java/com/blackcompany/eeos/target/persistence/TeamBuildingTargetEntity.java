package com.blackcompany.eeos.target.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = TeamBuildingTargetEntity.ENTITY_PREFIX)
@SQLDelete(sql = "UPDATE team_building_target SET is_deleted=true where team_building_target_id=?")
@Where(clause = "is_deleted=false")
public class TeamBuildingTargetEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "team_building_target";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_team_building_id", nullable = false)
	private Long teamBuildingId;

	@Column(name = ENTITY_PREFIX + "_member_id", nullable = false)
	private Long memberId;

	@Embedded @Builder.Default
	private TeamBuildingInputDataEntity inputData = TeamBuildingInputDataEntity.init();
}
