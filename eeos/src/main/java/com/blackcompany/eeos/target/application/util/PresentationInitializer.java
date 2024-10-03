package com.blackcompany.eeos.target.application.util;

import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import com.blackcompany.eeos.target.persistence.PresentationEntity;
import com.blackcompany.eeos.target.persistence.PresentationRepository;
import com.blackcompany.eeos.team.application.exception.NotFoundTeamException;
import com.blackcompany.eeos.team.persistence.TeamEntity;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    @Value("${eeos.team.list}")
    private String teamList;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("------------Presentation Initializer----------");
        Set<Long> programs = getPrograms().stream().map(ProgramEntity::getId).collect(Collectors.toSet());

        if(programs.isEmpty()) return;

        Set<Long> presentations = getPresentations().stream()
                .map(PresentationEntity::getProgramId)
                .collect(Collectors.toSet());

        Set<Long> target = programs.stream()
                .filter(programId -> !presentations.contains(programId))
                .collect(Collectors.toSet());

        List<Long> teams= getTeams().stream()
                .map(TeamEntity::getId)
                .toList();

        target.stream().forEach(targetId ->
                {
                    Set<PresentationEntity> entities =
                            teams.stream()
                                    .map(teamId -> PresentationEntity.builder()
                                            .teamId(teamId)
                                            .programId(targetId)
                                            .build())
                                    .collect(Collectors.toSet());
                    presentationRepository.saveAll(entities);
                });

        System.out.println("----------------------------------------------");
    }

    private List<String> getTeamList(){
        return Arrays.stream(teamList.split(",")).toList();
    }

    private List<ProgramEntity> getPrograms(){
        return programRepository.findAll();
    }

    private List<TeamEntity> getTeams(){
        List<String> teams = getTeamList();
        try {
            return teamRepository.findAllTeams().stream().filter(team -> teams.contains(team.getName())).toList();
        } catch (NotFoundTeamException e){
            return createTeams();
        }
    }

    private List<TeamEntity> createTeams(){
        Set<TeamEntity> teams = getTeamList().stream().map(team -> TeamEntity.builder().name(team).status(false).build()).collect(Collectors.toSet());
        return teamRepository.saveAll(teams);
    }

    private List<PresentationEntity> getPresentations(){
        return presentationRepository.findAll();
    }
}
