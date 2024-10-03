package com.blackcompany.eeos.target.application.util;

import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.target.persistence.PresentationEntity;
import com.blackcompany.eeos.target.persistence.PresentationRepository;
import com.blackcompany.eeos.team.persistence.TeamEntity;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// 기존에 저장된 Program과 Team을 매핑하는 구조
@Component
@RequiredArgsConstructor
@Slf4j
public class PresentationInitializer implements ApplicationRunner {

    private final PresentationRepository presentationRepository;
    private final ProgramRepository programRepository;
    private final TeamRepository teamRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("------------Presentation Initializer----------");
        Set<Long> programs = getPrograms().stream().map(ProgramEntity::getId).collect(Collectors.toSet());

        if(programs.isEmpty()) return;

        Set<Long> presentations = getPresentations().stream().map(PresentationEntity::getProgramId).collect(Collectors.toSet());

        Set<Long> target = programs.stream().filter(programId -> !presentations.contains(programId)).collect(Collectors.toSet());

        Long defaultTeamId = teamRepository.findById(0L).orElseThrow().getId();

        Set<PresentationEntity> entities = target.stream().map(targetId -> PresentationEntity.builder().teamId(defaultTeamId).programId(targetId).build()).collect(Collectors.toSet());

        presentationRepository.saveAll(entities);
        System.out.println("----------------------------------------------");
    }

    private List<ProgramEntity> getPrograms(){
        return programRepository.findAll();
    }

    private List<PresentationEntity> getPresentations(){
        return presentationRepository.findAll();
    }
}
