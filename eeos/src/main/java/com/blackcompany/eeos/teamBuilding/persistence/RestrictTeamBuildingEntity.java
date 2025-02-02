package com.blackcompany.eeos.teamBuilding.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
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
@Table(name = RestrictTeamBuildingEntity.ENTITY_PREFIX)
@SQLDelete(
		sql = "UPDATE restrict_team_building SET is_deleted=true where restrict_team_building_id=?")
@Where(clause = "is_deleted=false")
public class RestrictTeamBuildingEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "restrict_team_building";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_total_active_count", nullable = false)
	@Builder.Default
	private Long totalActiveCount = 0L;

	@Version private Long version;
}
