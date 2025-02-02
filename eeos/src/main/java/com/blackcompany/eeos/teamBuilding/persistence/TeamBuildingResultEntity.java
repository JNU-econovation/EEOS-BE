package com.blackcompany.eeos.teamBuilding.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.common.persistence.IntegerArrayConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
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
@Table(name = TeamBuildingResultEntity.ENTITY_PREFIX)
@SQLDelete(sql = "UPDATE team_building_result SET is_deleted=true where team_building_result_id=?")
@Where(clause = "is_deleted=false")
public class TeamBuildingResultEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "team_building_result";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_member_ids", nullable = false)
	@Convert(converter = IntegerArrayConverter.class)
	private List<Long> memberIds;

	@Column(name = ENTITY_PREFIX + "_status", nullable = false)
	private Long teamBuildingId;
}
