package com.blackcompany.eeos.member.event;

public class DeletedMemberEvent {
	private final Long memberId;

	private DeletedMemberEvent(Long memberId) {
		this.memberId = memberId;
	}

	public static DeletedMemberEvent of(Long memberId) {
		return new DeletedMemberEvent(memberId);
	}
}
