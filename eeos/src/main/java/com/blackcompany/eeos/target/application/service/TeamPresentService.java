package com.blackcompany.eeos.target.application.service;

import com.blackcompany.eeos.target.application.usecase.PresentTeamUsecase;
import com.blackcompany.eeos.target.persistence.PresentationEntity;
import com.blackcompany.eeos.target.persistence.PresentationRepository;
import com.blackcompany.eeos.target.persistence.converter.PresentationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamPresentService implements PresentTeamUsecase {

    private final PresentationRepository presentationRepository;
    private final PresentationConverter presentationConverter;

    @Override
    public void save(Long programId, List<Long> teamIds) {

        List<PresentationEntity> entities = toEntities(programId, teamIds);

        entities.stream()
                .map(e -> presentationRepository.save(e))
                .collect(Collectors.toList());

    }

    private List<PresentationEntity> toEntities(Long programId, List<Long> teamIds){
        return teamIds.stream()
                .map(id -> presentationConverter.from(programId, id))
                .collect(Collectors.toList());
    }
}
