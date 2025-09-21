package com.blackcompany.eeos.program.application.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatedProgramEvent {

    private final Long programId;

    public static CreatedProgramEvent of(Long programId){
        return new CreatedProgramEvent(programId);
    }
}
