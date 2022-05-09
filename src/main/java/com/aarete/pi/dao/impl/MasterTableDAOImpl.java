package com.aarete.pi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import com.aarete.pi.enums.Role;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import com.aarete.pi.bean.UserRequest;
import com.aarete.pi.dao.MasterTableDAO;
import com.aarete.pi.entity.QUserEntity;
import com.aarete.pi.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.util.CollectionUtils;

@Component
public class MasterTableDAOImpl implements MasterTableDAO {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public List<UserEntity> getUsers(@Valid UserRequest userRequest, String userId) {

		final JPAQuery<UserEntity> query = new JPAQuery<>(entityManager);
		final QUserEntity qUserEntity = QUserEntity.userEntity;
		BooleanBuilder queryBulBuilder = new BooleanBuilder();
		//Share Playlists - System must allow Playlists to be shared only within Aarete users and Aarete Managers
		// OR within client users and client managers within a client engagement.
		//Note - Playlist will be shared with users and not groups

		if(Role.AARETE_USER.name().equals(userRequest.getEngagementRole()) || Role.AARETE_MANAGER.name().equals(userRequest.getEngagementRole()))
			queryBulBuilder.and(qUserEntity.userType.in(Role.AARETE_USER.name(), Role.AARETE_MANAGER.name()));

		if(Role.CLIENT_USER.name().equals(userRequest.getEngagementRole()) || Role.CLIENT_MANAGER.name().equals(userRequest.getEngagementRole()))
			queryBulBuilder.and(qUserEntity.userType.in(Role.CLIENT_USER.name(), Role.CLIENT_MANAGER.name()));

		queryBulBuilder.and(qUserEntity.userId.notIn(userId));
		return query.select(qUserEntity).from(qUserEntity).where(queryBulBuilder.getValue())
				.fetch();
	}

	@Override
	public List<String> getUserNames(@Valid List<String> userIds) {
		BooleanBuilder builder = new BooleanBuilder();
		final JPAQuery<UserEntity> query = new JPAQuery<>(entityManager);
		final QUserEntity qUserEntity = QUserEntity.userEntity;
		if (!CollectionUtils.isEmpty(userIds)) {
			builder.and(qUserEntity.userId.in(userIds));
		}
		return query.select(qUserEntity.userName).from(qUserEntity).where(builder.getValue()).fetch();
	}
}