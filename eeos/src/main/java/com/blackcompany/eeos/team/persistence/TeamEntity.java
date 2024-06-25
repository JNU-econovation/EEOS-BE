package com.blackcompany.eeos.team.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity
@Table(
		name = TeamEntity.ENTITY_PREFIX,
		indexes = {
			@Index(name = "idx_team_name", columnList = "team_name"),
			@Index(name = "idx_team_status", columnList = "team_status")
		})
// @Where(clause = "status=1")
/** status=1, 현학기 활동 팀 status=0, 현학기 활동 팀 X */
public class TeamEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "team";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(unique = true, name = ENTITY_PREFIX + "_name", nullable = false)
	private String name;

	@Column(
			name = ENTITY_PREFIX + "_status",
			nullable = false,
			columnDefinition = "boolean default 1")
	@Builder.Default
	private boolean status = true; // 현학기 활동팀 1, 아니면 0

	public void updateTeamStatus(boolean status) {
		this.status = status;
	}
}
