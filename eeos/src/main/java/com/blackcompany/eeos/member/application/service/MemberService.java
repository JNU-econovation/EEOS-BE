package com.blackcompany.eeos.member.application.service;

import com.blackcompany.eeos.attend.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.MemberModel;
import com.blackcompany.eeos.member.application.model.converter.MemberEntityConverter;
import com.blackcompany.eeos.member.persistence.MemberEntity;
import com.blackcompany.eeos.member.persistence.MemberRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberEntityConverter entityConverter;
	private final MemberRepository memberRepository;

	public List<MemberModel> findAllMember() {
		return memberRepository.findAll().stream()
				.map(entityConverter::from)
				.collect(Collectors.toList());
	}

	public MemberModel findMemberInfo(final Long memberId) {
		MemberEntity entity =
				memberRepository
						.findById(memberId)
						.orElseThrow(() -> new NotFoundMemberException("존재하지 않는 멤버입니다."));

		return entityConverter.from(entity);
	}
}
