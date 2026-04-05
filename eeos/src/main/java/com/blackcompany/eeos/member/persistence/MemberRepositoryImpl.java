package com.blackcompany.eeos.member.persistence;

import com.blackcompany.eeos.auth.application.domain.OauthServerType;
import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.application.repository.MemberRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
	private final JpaMemberRepository jpaRepository;
	private final MemberEntityConverter converter;
	private final JpaMemberCustomRepository jpaMemberCustomRepository;

	@Override
	public List<MemberModel> findMembersByProgramId(Long programId) {
		return jpaRepository.findMembersByProgramId(programId).stream().map(converter::from).toList();
	}

	@Override
	public List<MemberModel> findMembersByActiveStatus(ActiveStatus activeStatus) {
		return jpaRepository.findMembersByActiveStatus(activeStatus).stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public List<MemberModel> findMembers() {
		return jpaRepository.findMembers().stream().map(converter::from).toList();
	}

	@Override
	public List<MemberModel> findMembersByIds(List<Long> ids) {
		return jpaRepository.findMembersByIds(ids).stream().map(converter::from).toList();
	}

	@Override
	public List<MemberModel> findMembersByIdsInOrder(List<Long> ids) {
		return jpaMemberCustomRepository.findMembersByIdsInOrder(ids).stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public MemberModel findById(Long memberId) {
		return jpaRepository
				.findById(memberId)
				.map(converter::from)
				.orElseThrow(NotFoundMemberException::new);
	}

	@Override
	public Boolean existsById(Long memberId) {
		return jpaRepository.existsById(memberId);
	}

	@Override
	public MemberModel save(MemberModel model) {
		return converter.from(jpaRepository.save(converter.toEntity(model)));
	}

	@Override
	public void deleteById(Long memberId) {
		jpaRepository.deleteById(memberId);
	}

	@Override
	public String findNameById(Long memberId) {
		return jpaRepository.findNameById(memberId).orElseThrow(NotFoundMemberException::new);
	}

	@Override
	public List<MemberModel> findSlackOnlyMembers() {
		return jpaRepository.findSlackOnlyMembers(OauthServerType.SLACK).stream()
				.map(converter::from)
				.toList();
	}

	@Override
	public List<MemberModel> findSlackOnlyMembersByGeneration(int generation) {
		String generationPrefix = generation + "기 %";
		return jpaRepository
				.findSlackOnlyMembersByGeneration(OauthServerType.SLACK, generationPrefix)
				.stream()
				.map(converter::from)
				.toList();
	}
}
