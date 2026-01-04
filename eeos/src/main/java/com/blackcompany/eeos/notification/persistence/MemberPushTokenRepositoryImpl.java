package com.blackcompany.eeos.notification.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.blackcompany.eeos.notification.application.model.MemberPushTokenModel;
import com.blackcompany.eeos.notification.application.model.NotificationProvider;
import com.blackcompany.eeos.notification.application.model.converter.MemberPushTokenEntityConverter;
import com.blackcompany.eeos.notification.application.respository.MemberPushTokenRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberPushTokenRepositoryImpl implements MemberPushTokenRepository {

	private final JpaMemberPushTokenRepository jpaRepository;

	private final MemberPushTokenEntityConverter converter;

	@Override
	public Optional<MemberPushTokenModel> findByPushToken(String pushToken) {
		return jpaRepository.findByPushToken(pushToken).map(converter::from);
	}

	@Override
	public List<MemberPushTokenModel> findByMemberId(Long memberId) {
		return jpaRepository.findByMemberId(memberId).stream().map(converter::from).toList();
	}

	@Override
	public List<MemberPushTokenModel> findByMemberIdAndProvider(Long memberId, NotificationProvider provider) {
		return jpaRepository.findByMemberIdAndProvider(memberId, provider).stream().map(converter::from).toList();
	}

	@Override
	public void deleteByPushToken(String pushToken) {
		jpaRepository.deleteByPushToken(pushToken);

	}

	@Override
	public int deleteByMemberId(Long memberId) {
		return jpaRepository.deleteByMemberId(memberId);
	}

	@Override
	public int deleteByUpdatedDateBefore(LocalDateTime updatedDate) {
		Timestamp timestamp = Timestamp.valueOf(updatedDate);
		return jpaRepository.deleteByUpdatedDateBefore(timestamp);
	}

	@Override
	public MemberPushTokenModel save(MemberPushTokenModel memberPushToken) {
		MemberPushTokenEntity memberPushTokenEntity = converter.toEntity(memberPushToken);
		MemberPushTokenEntity saved = jpaRepository.save(memberPushTokenEntity);
		return converter.from(saved);



	}

}
