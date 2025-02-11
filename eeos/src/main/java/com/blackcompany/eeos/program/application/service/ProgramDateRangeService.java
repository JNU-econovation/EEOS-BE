package com.blackcompany.eeos.program.application.service;

import com.blackcompany.eeos.common.utils.DateConverter;
import com.blackcompany.eeos.program.application.model.ProgramModel;
import com.blackcompany.eeos.program.application.model.converter.ProgramEntityConverter;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramDateRangeService {

    private final ProgramRepository programRepository;
    private final ProgramEntityConverter programEntityConverter;

    public List<ProgramModel> getPrograms(long startDate, long endDate, int size, int page) {
        Pageable pageable = PageRequest.of(page, size);

        Timestamp startDateTimestamp = DateConverter.toEpochSecond(LocalDate.from(Instant.ofEpochMilli(startDate)));
        Timestamp endDateTimestamp = DateConverter.toEpochSecond(LocalDate.from(Instant.ofEpochMilli(endDate)));

        Page<ProgramEntity> pages = programRepository.findByDateRange(startDateTimestamp, endDateTimestamp, pageable);

        return pages.get()
                .map(programEntityConverter::from)
                .toList();
    }

}
