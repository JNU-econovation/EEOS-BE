package com.blackcompany.eeos.program.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import com.blackcompany.eeos.program.application.model.ProgramAttendMode;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.*;
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
		name = ProgramEntity.ENTITY_PREFIX,
		indexes = @Index(name = "idx_program_date", columnList = "program_date"))
@SQLDelete(sql = "UPDATE program SET is_deleted=true where program_id=?")
// delete from program where program_id=1;
@Where(clause = "is_deleted=false")
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

	@Column(name = ENTITY_PREFIX + "_url", nullable = false)
	private String githubUrl;

	@Column(name = ENTITY_PREFIX + "_category", nullable = false)
	@Enumerated(EnumType.STRING)
	private ProgramCategory programCategory;

	@Column(name = ENTITY_PREFIX + "_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ProgramType programType;

	@Column(name = ENTITY_PREFIX + "_attend_mode", nullable = false)
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private ProgramAttendMode attendMode = ProgramAttendMode.NONE;

	@Column(name = ENTITY_PREFIX + "_writer", nullable = false)
	private Long writer;
}
