package com.blackcompany.eeos.calendar.persistence;

import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.model.CalendarType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
        name = CalendarEntity.ENTITY_PREFIX,
        indexes = {
                @Index(name = "idx_calendar_start_at", columnList = "calendar_start_at"),
                @Index(name = "idx_calendar_end_at", columnList = "calendar_end_at")
        })
public class CalendarEntity {

    public static final String ENTITY_PREFIX = "calendar";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ENTITY_PREFIX + "_id", nullable = false)
    private Long id;

    @Column(name = ENTITY_PREFIX + "_writer", nullable = false)
    private Long writer;

    @Enumerated(EnumType.STRING)
    @Column(name = ENTITY_PREFIX + "_type", nullable = false)
    private CalendarType type;

    @Column(name = ENTITY_PREFIX + "_title", nullable = false)
    private String title;

    @Column(name = ENTITY_PREFIX + "_url", nullable = false)
    private String url;

    @Column(name = ENTITY_PREFIX + "_start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = ENTITY_PREFIX + "_end_at", nullable = false)
    private LocalDateTime endAt;

    public CalendarModel toModel(){
        return CalendarModel.builder()
                .title(this.title)
                .url(this.url)
                .endAt(this.endAt)
                .startAt(this.startAt)
                .id(this.id)
                .writer(this.writer)
                .type(this.type)
                .build();
    }

    public static CalendarEntity toEntity(CalendarModel model){
        return CalendarEntity.builder()
                .title(model.getTitle())
                .url(model.getUrl())
                .endAt(model.getEndAt())
                .startAt(model.getStartAt())
                .writer(model.getWriter())
                .type(model.getType())
                .build();
    }
}
