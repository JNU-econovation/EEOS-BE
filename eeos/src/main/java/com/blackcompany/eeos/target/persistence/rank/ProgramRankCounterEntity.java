package com.blackcompany.eeos.target.persistence.rank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = ProgramRankCounterEntity.ENTITY_PREFIX)
public class ProgramRankCounterEntity {
	public static final String ENTITY_PREFIX = "program_rank_counter";

	@Id
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long programId;

	@Column(name = ENTITY_PREFIX + "_nextRank", nullable = false)
	private Long nextRank;

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public void setNextRank(Long nextRank) {
		this.nextRank = nextRank;
	}

	public void incrementNextRank() {
		this.nextRank = this.nextRank + 1;
	}
}
