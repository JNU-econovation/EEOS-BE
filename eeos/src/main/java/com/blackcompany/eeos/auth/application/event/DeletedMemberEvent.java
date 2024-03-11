package com.blackcompany.eeos.auth.application.event;

import lombok.Getter;

@Getter
public class DeletedMemberEvent {
	private final Long memberId;
	private final String token;

	private DeletedMemberEvent(Long memberId, String token) {
		this.memberId = memberId;
		this.token = token;
	}

	/**
	 * 멤버 탈퇴 이벤트를 발행한다.
	 *
	 * @param memberId 회원 탈퇴를 원하는 멤버 id
	 * @param token 회원 탈퇴 시 사용한 토큰
	 * @return
	 */
	public static DeletedMemberEvent of(Long memberId, String token) {
		return new DeletedMemberEvent(memberId, token);
	}
}
