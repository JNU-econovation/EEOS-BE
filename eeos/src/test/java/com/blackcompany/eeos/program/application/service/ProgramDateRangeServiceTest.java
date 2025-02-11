package com.blackcompany.eeos.program.application.service;


import com.blackcompany.eeos.program.application.model.ProgramModel;
import static org.mockito.Mockito.verify;

import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.bouncycastle.util.Times;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ProgramDateRangeServiceTest {

    @InjectMocks ProgramDateRangeService programDateRangeService;

    @Mock ProgramRepository programRepository;

    @Test
    @DisplayName("특정범위_사이의_행사_리스트를_조회한다.")
    void get_program_date_range() {
        // given
        Timestamp startDate = Timestamp.from(Instant.now());
        Timestamp endDate = Timestamp.from(Instant.now().plus(Duration.ofDays(1)));
        int page = 1;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);

        // when
        Page<ProgramEntity> pages = programRepository.findByDateRange(startDate, endDate, pageable);

        // then
        verify(programRepository).findByDateRange(startDate, endDate, pageable);

    }
}
