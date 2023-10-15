package com.blackcompany.eeos.common.persistence;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, SoftDeleteListener.class})
@SuperBuilder(toBuilder = true)
public class BaseEntity {

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Timestamp createdDate;

	@LastModifiedDate
	@Column(nullable = false)
	private Timestamp updatedDate;

	@Builder.Default
	@Column(nullable = false)
	private boolean isDeleted = false;

	public void delete() {
		this.isDeleted = true;
	}
}
