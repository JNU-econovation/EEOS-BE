package com.blackcompany.eeos.team.application.util;

import com.blackcompany.eeos.team.persistence.TeamEntity;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


// 기존 데이터베이스와의 데이터 정합성을 위한 클래스
@RequiredArgsConstructor
@Slf4j
@Component
public class TeamInitializer implements ApplicationRunner {

    private final TeamRepository teamRepository;

    @Value("${eeos.team.list}")
    private String teamList;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-----------Team Initializer-----------");
        Arrays.stream(teamList.split(",")).forEach(log::info);
        try {
            Set<TeamEntity> newTeams = Arrays.stream(teamList.split(","))
                    .map(teamName -> TeamEntity.builder().name(teamName).status(false).build())
                    .collect(Collectors.toSet());
            teamRepository.saveAll(newTeams);
            log.info("임시 팀이 생성되었습니다.");
        } catch (Exception e){
            log.error("임시 팀이 생성되지 않았습니다.");
        }
        System.out.println("---------------------------------------");

    }

    private boolean isEmptyTable(){
        return teamRepository.findAllTeams().isEmpty();
    }
}
