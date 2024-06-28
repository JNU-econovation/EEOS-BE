package com.blackcompany.eeos.auth.persistence;

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
		name = AccountEntity.ENTITY_PREFIX,
		indexes = {@Index(name = "idx_member_id", columnList = "account_member_id")})
public class AccountEntity extends BaseEntity {

	public static final String ENTITY_PREFIX = "account";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_member_id", nullable = false, unique = true)
	private Long memberId;

	@Column(name = ENTITY_PREFIX + "_login_id", nullable = false, unique = true)
	private String loginId;

	@Column(name = ENTITY_PREFIX + "_login_passwd", nullable = false)
	private String passWd;
}
