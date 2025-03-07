package com.blackcompany.eeos.member.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaMemberCustomRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<MemberEntity> findMembersByIdsInOrder(List<Long> idList) {

        StringBuilder ids = new StringBuilder();

        for(Long id : idList) {
            ids.append(id).append(",");
        }

        ids.deleteCharAt(ids.length()-1);

        String jpql = "SELECT m FROM MemberEntity m WHERE m.id IN :ids AND m.isDeleted=false ORDER BY FUNCTION('FIELD', m.id, '%Ids%') ";

        jpql = jpql.replace("'%Ids%'", ids.toString());

        List<MemberEntity> result = em.createQuery(jpql, MemberEntity.class)
                .setParameter("ids", idList)
                .getResultList();

        return result;

    }

}
