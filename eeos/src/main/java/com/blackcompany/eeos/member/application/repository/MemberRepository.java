package com.blackcompany.eeos.member.application.repository;

import com.blackcompany.eeos.member.application.exception.NotFoundMemberException;
import com.blackcompany.eeos.member.application.model.ActiveStatus;
import com.blackcompany.eeos.member.application.model.MemberModel;
import java.util.List;

public interface MemberRepository {
	List<MemberModel> findMembersByProgramId(Long programId);

	List<MemberModel> findMembersByActiveStatus(ActiveStatus activeStatus);

	List<MemberModel> findMembers();

	List<MemberModel> findMembersByIds(List<Long> ids);

	List<MemberModel> findMembersByIdsInOrder(List<Long> ids);

	/**
	 * @throws NotFoundMemberException
	 */
	MemberModel findById(Long memberId);

	Boolean existsById(Long memberId);

	MemberModel save(MemberModel model);

	void deleteById(Long memberId);

	String findNameById(Long memberId);

	List<MemberModel> findSlackOnlyMembers();
}
