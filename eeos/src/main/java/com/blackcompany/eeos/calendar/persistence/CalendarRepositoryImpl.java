package com.blackcompany.eeos.calendar.persistence;

import com.blackcompany.eeos.calendar.application.model.CalendarModel;
import com.blackcompany.eeos.calendar.application.repository.CalendarRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalendarRepositoryImpl implements CalendarRepository {

    private final CalendarJpaRepository jpaRepository;

    @Override
    public Long save(CalendarModel calendar) {
        CalendarEntity entity = CalendarEntity.toEntity(calendar);
        return jpaRepository.save(entity).getId();
    }

    @Override
    public CalendarModel findById(Long id) {
        Optional<CalendarEntity> entity = jpaRepository.findById(id);

        return entity.map(CalendarEntity::toModel)
                .orElseThrow();
    }
}
