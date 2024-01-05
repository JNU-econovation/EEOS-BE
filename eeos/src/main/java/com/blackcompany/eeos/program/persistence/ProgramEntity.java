package com.blackcompany.eeos.program.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
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
@Table(
		name = ProgramEntity.ENTITY_PREFIX,
		indexes = @Index(name = "idx_program_date", columnList = "program_date"))
public class ProgramEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "program";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_title", nullable = false)
	private String title;

	@Column(name = ENTITY_PREFIX + "_content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = ENTITY_PREFIX + "_date", nullable = false)
	private Timestamp programDate;

	@Column(name = ENTITY_PREFIX + "_category", nullable = false)
	private ProgramCategory programCategory;

	@Column(name = ENTITY_PREFIX + "_type", nullable = false)
	private ProgramType programType;

	@Column(name = ENTITY_PREFIX + "_writer", nullable = false)
	private Long writer;
}
