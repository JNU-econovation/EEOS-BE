package com.blackcompany.eeos.program.application.util;

import com.blackcompany.eeos.program.application.model.ProgramAttendMode;
import com.blackcompany.eeos.program.persistence.ProgramEntity;
import com.blackcompany.eeos.program.persistence.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProgramInitializer implements ApplicationRunner {

    private final ProgramRepository programRepository;
    private final String defaultUrl = "https://github.com/JNU-econovation/weekly_presentation/tree/2024-1/2024-1/A_team/1st";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("------------Program Initializer----------");
        List<ProgramEntity> programs = getPrograms();

        if(programs.isEmpty()) return;

        programs = programs.stream()
                .map(program -> {
                                        if(program.getGithubUrl()==null){
                                            return program.toBuilder().githubUrl(defaultUrl).build();
                                        }
                                        return program;
                })
                .map(program -> {
                                        if(program.getAttendMode()==null){
                                            return program.toBuilder().attendMode(ProgramAttendMode.END).build();
                                        }

                                        return program;
                })
                .collect(Collectors.toList());

        programRepository.saveAll(programs);
        System.out.println("----------------------------------------------");
    }

    private List<ProgramEntity> getPrograms(){
        return programRepository.findAll();
    }
}
