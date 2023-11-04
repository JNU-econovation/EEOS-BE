package com.blackcompany.eeos.member.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
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
		name = MemberEntity.ENTITY_PREFIX,
		indexes = @Index(name = "idx_generation_name", columnList = "member_generation,member_name"))
public class MemberEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "member";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_name", nullable = false)
	private String name;

	@Column(name = ENTITY_PREFIX + "_generation", nullable = false)
	private Long generation;
}
