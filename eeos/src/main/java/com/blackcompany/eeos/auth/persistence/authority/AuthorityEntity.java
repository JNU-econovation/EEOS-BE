package com.blackcompany.eeos.auth.persistence.authority;

import com.blackcompany.eeos.auth.application.model.Role;
import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@SuperBuilder(toBuilder = true)
@Table(
		name = AuthorityEntity.NAME,
		indexes = {@Index(name = "idx_member_id", columnList = AuthorityEntity.NAME + "_member_id")},
		uniqueConstraints = @UniqueConstraint(
				name = "uk_authority_member_role",
				columnNames = {
						AuthorityEntity.NAME + "_member_id",
						AuthorityEntity.NAME + "_role"
				})
)

@Getter
public class AuthorityEntity extends BaseEntity {

	public static final String NAME = "authority";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = NAME + "_member_id")
	private Long memberId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = NAME + "_role")
	private Role role;
}
