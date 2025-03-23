package com.blackcompany.eeos.member.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaMemberCustomRepository {

	@PersistenceContext private final EntityManager em;

	public List<MemberEntity> findMembersByIdsInOrder(List<Long> idList) {
		if(!idList.isEmpty()) {

			String ids = String.join(",", idList.stream().map(String::valueOf).toList());

			String jpql =
					"SELECT m FROM MemberEntity m WHERE m.id IN :ids AND m.isDeleted=false ORDER BY FUNCTION('FIELD', m.id, '%Ids%') "
							.replace("'%Ids%'", ids);

			List<MemberEntity> result =
					em.createQuery(jpql, MemberEntity.class).setParameter("ids", idList).getResultList();

			return result;
		}

		return List.of();
	}
}
