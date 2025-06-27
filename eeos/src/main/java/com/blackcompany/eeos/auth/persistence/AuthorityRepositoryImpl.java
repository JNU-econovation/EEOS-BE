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
	public Set<AuthorityModel> findByMemberId(Long memberId) {
		return repository.findByMemberId(memberId).stream()
				.map(this::toModel)
				.collect(Collectors.toSet());
	}

	@Override
	public Long save(AuthorityModel authorityModel) {
		return repository.save(toEntity(authorityModel)).getId();
	}

	private AuthorityEntity toEntity(AuthorityModel model) {
		return AuthorityEntity.builder().memberId(model.getMemberId()).role(model.getName()).build();
	}

	private AuthorityModel toModel(AuthorityEntity entity) {
		return AuthorityModel.builder()
				.id(entity.getId())
				.memberId(entity.getMemberId())
				.name(entity.getRole())
				.build();
	}
}
