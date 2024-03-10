package com.blackcompany.eeos.auth.application.event;

import lombok.Getter;

@Getter
public class DeletedMemberEvent {
	private final Long memberId;

	private DeletedMemberEvent(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * 멤버 탈퇴 이벤트를 발행한다.
	 *
	 * @param memberId 회원 탈퇴를 원하는 멤버 id
	 * @return
	 */
	public static DeletedMemberEvent of(Long memberId) {
		return new DeletedMemberEvent(memberId);
	}
}
