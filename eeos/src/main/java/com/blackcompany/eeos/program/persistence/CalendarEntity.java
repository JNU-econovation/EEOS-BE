package com.blackcompany.eeos.program.persistence;

import com.blackcompany.eeos.common.persistence.BaseEntity;
import jakarta.persistence.*;
import java.sql.Timestamp;
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
		name = CalendarEntity.ENTITY_PREFIX,
		indexes = {
			@Index(
					name = "idx_calendar_created_date_id",
					columnList = "createdDate DESC, calendar_id DESC")
		})
@SQLDelete(sql = "UPDATE calendar SET is_deleted=true where calendar_id=?")
@Where(clause = "is_deleted=false")
public class CalendarEntity extends BaseEntity {
	public static final String ENTITY_PREFIX = "calendar";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = ENTITY_PREFIX + "_id", nullable = false)
	private Long id;

	@Column(name = ENTITY_PREFIX + "_start_date", nullable = false)
	private Timestamp startDate;

	@Column(name = ENTITY_PREFIX + "_end_date", nullable = false)
	private Timestamp endDate;
}
