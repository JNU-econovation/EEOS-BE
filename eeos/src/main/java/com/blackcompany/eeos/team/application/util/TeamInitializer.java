package com.blackcompany.eeos.team.application.util;

import com.blackcompany.eeos.team.persistence.TeamEntity;
import com.blackcompany.eeos.team.persistence.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


// 기존 데이터베이스와의 데이터 정합성을 위한 클래스
@RequiredArgsConstructor
@Slf4j
@Component
public class TeamInitializer implements ApplicationRunner {

    private final TeamRepository teamRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-----------Team Initializer-----------");
        if(isEmptyTable()){
            TeamEntity defaultTeam = TeamEntity.builder().id(0L).name("임시 활동 팀").status(false).build();
            teamRepository.save(defaultTeam);
            log.info("임시 팀이 생성되었습니다.");
        }
        else log.info("임시 팀이 생성되지 않았습니다.");
        System.out.println("---------------------------------------");

    }

    private boolean isEmptyTable(){
        return teamRepository.findAllTeams().isEmpty();
    }
}
