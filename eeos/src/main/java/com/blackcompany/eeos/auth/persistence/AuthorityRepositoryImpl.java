package com.blackcompany.eeos.auth.persistence;

import com.blackcompany.eeos.auth.application.model.AuthorityModel;
import com.blackcompany.eeos.auth.application.repository.AuthorityRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorityRepositoryImpl implements AuthorityRepository {

    private final AuthorityJpaRepository repository;

    @Override
    public Set<AuthorityModel> findByAuthority(Long memberId) {
        return repository.findByMemberId(memberId)
                .stream()
                .map(this::toModel)
                .collect(Collectors.toSet());
    }

    private AuthorityModel toModel(Authority entity) {
        return AuthorityModel.builder()
                .id(entity.getId())
                .memberId(entity.getMemberId())
                .name(entity.getRole())
                .build();
    }
}
