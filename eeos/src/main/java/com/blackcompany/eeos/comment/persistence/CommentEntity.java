package com.blackcompany.eeos.comment.persistence;

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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Table(name = CommentEntity.ENTITY_PREFIX)
@SQLDelete(sql = "UPDATE comment SET is_deleted=true where comment_id=?")
@Where(clause = "is_deleted=false")
public class CommentEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "comment";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_program_id", nullable = false)
	private Long programId;

	@Column(name = ENTITY_PREFIX + "_team", nullable = false)
	private Long presentingTeamId;

	@Column(name = ENTITY_PREFIX + "_writer", nullable = false)
	private Long writer;

	@Column(name = ENTITY_PREFIX + "_content", nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(name = ENTITY_PREFIX + "_super_comment_id", nullable = false)
	private Long superCommentId;

	@Column(name = ENTITY_PREFIX + "_isChecked", nullable = false)
	@ColumnDefault("0")
	private Boolean isChecked;
}
