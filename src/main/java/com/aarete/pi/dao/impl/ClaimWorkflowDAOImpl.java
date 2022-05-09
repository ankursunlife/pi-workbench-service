package com.aarete.pi.dao.impl;

import com.aarete.pi.bean.ClaimStatusRequest;
import com.aarete.pi.dao.ClaimWorkflowDAO;
import com.aarete.pi.entity.QClaimLineEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ClaimWorkflowDAOImpl implements ClaimWorkflowDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public long updateClaimStatusCode(ClaimStatusRequest claimStatusRequest) {
        BooleanBuilder builder = new BooleanBuilder();
        JPAQueryFactory jpaQuery = new JPAQueryFactory(entityManager);
        final QClaimLineEntity qClaimLineEntity = QClaimLineEntity.claimLineEntity;

        builder.and(qClaimLineEntity.claimLineId.eq(claimStatusRequest.getClaimLineId()))
                .and(qClaimLineEntity.claimNum.eq(claimStatusRequest.getClaimNum()))
                .and(qClaimLineEntity.claimLineNum.eq(claimStatusRequest.getClaimLineNum()))
                .and(qClaimLineEntity.engagementId.eq(claimStatusRequest.getEngagementId()));

        return jpaQuery.update(qClaimLineEntity)
                .where(builder.getValue())
                .set(qClaimLineEntity.claimStatusCode, claimStatusRequest.getClaimStatusCode())
                .execute();
    }
}
