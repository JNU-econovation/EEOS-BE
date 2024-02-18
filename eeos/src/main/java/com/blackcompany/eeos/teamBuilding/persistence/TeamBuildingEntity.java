package com.blackcompany.eeos.teamBuilding.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = TeamBuildingEntity.ENTITY_PREFIX)
@SQLDelete(sql = "UPDATE team_building SET is_deleted=true where team_building_id=?")
@Where(clause = "is_deleted=false")
public class TeamBuildingEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "team_building";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_title", nullable = false)
	private String title;

	@Column(name = ENTITY_PREFIX + "_content", nullable = false)
	private String content;

	@Column(name = ENTITY_PREFIX + "_max_team_size", nullable = false)
	private int maxTeamSize;

	@Column(name = ENTITY_PREFIX + "_status", nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private TeamBuildingStatus status = TeamBuildingStatus.PROGRESS;

	@Column(name = ENTITY_PREFIX + "_member_id", nullable = false)
	private Long memberId;
}
