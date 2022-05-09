package com.aarete.pi.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import com.aarete.pi.entity.PlaylistClaimlineEntity;
import com.aarete.pi.entity.PlaylistEntity;
import com.aarete.pi.entity.QClaimLineEntity;
import com.aarete.pi.entity.QPlaylistClaimlineEntity;
import com.aarete.pi.entity.QPlaylistEntity;
import com.aarete.pi.entity.QSharedPlaylistEntity;
import com.aarete.pi.entity.SharedPlaylistEntity;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.repository.PlaylistRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aarete.pi.bean.Playlist;
import com.aarete.pi.dao.PlaylistDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static com.aarete.pi.constant.WorkbenchConstants.PLAYLIST_STATUS_ACTIVE;
import static com.aarete.pi.controller.MasterTableController.RECORD_NOT_FOUND;

@Component
public class PlaylistDAOImpl implements PlaylistDAO{


    @PersistenceContext
    private EntityManager entityManager;

	@Autowired
	private PlaylistRepository playlistRepository;

	@Override
	public void addPlaylist(Playlist playlistRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Playlist> getAllMyPlayList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePlayList(Playlist playlistRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long deletePlayList(long playlistId) {
		playlistRepository.deleteById(playlistId);
		// deleting playlist in shared playlist.
		BooleanBuilder builder = new BooleanBuilder();
		final JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		final QSharedPlaylistEntity qSharedPlaylistEntity = QSharedPlaylistEntity.sharedPlaylistEntity;
		builder.and(qSharedPlaylistEntity.playlistId.eq(playlistId));
		return queryFactory.delete(qSharedPlaylistEntity).where(builder.getValue()).execute();
	}

	@Override
	public List<Tuple> getPlaylist(long playlistId) {
		BooleanBuilder builder = new BooleanBuilder();
		final JPAQuery<PlaylistClaimlineEntity> query = new JPAQuery<>(entityManager);
		final QPlaylistClaimlineEntity qPlayListClaimLineEntity = QPlaylistClaimlineEntity.playlistClaimlineEntity;
		final QPlaylistEntity qPlaylistEntity = QPlaylistEntity.playlistEntity;
		final QClaimLineEntity qClaimLineEntity = QClaimLineEntity.claimLineEntity;

		builder.and(qPlaylistEntity.playlistId.eq(playlistId));
		builder.and(qClaimLineEntity.claimStatusCode.notLike("%CLOSED%").or(qClaimLineEntity.claimStatusCode.isNull()));

		List<Tuple> fetch = query.select(qClaimLineEntity, qPlaylistEntity).from(qPlaylistEntity)
				.join(qPlayListClaimLineEntity).on(qPlaylistEntity.playlistId.eq(qPlayListClaimLineEntity.playlistId))
				.join(qClaimLineEntity).on(qClaimLineEntity.claimLineId.eq(qPlayListClaimLineEntity.claimLineId))
				.where(builder.getValue()).fetch();
		return fetch;
	}

	@Override
	public void sharePlayList(@Valid Playlist playlistRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Tuple> getAllPlaylists(String loggedInUserId) {

		BooleanBuilder builder = new BooleanBuilder();
		final JPAQuery<PlaylistEntity> query = new JPAQuery<>(entityManager);
		final QPlaylistEntity qPlaylistEntity = QPlaylistEntity.playlistEntity;
		final QPlaylistClaimlineEntity qPlayListClaimLineEntity = QPlaylistClaimlineEntity.playlistClaimlineEntity;
		final QClaimLineEntity qClaimLineEntity = QClaimLineEntity.claimLineEntity;

		if (StringUtils.hasText(loggedInUserId)) {
			builder.and(qPlaylistEntity.playlistOwnerId.in(loggedInUserId));
		}

		builder.and(qPlaylistEntity.playlistStatus.in(PLAYLIST_STATUS_ACTIVE));
		// show only non-closed, non-waiting claims in playlist screen
		builder.and(qClaimLineEntity.claimStatusCode.notLike("%CLOSED%")
				.or(qClaimLineEntity.claimStatusCode.isNull()))
				.or(qClaimLineEntity.claimStatusCode.notLike("%WAITING%"));

		return query.groupBy(qPlaylistEntity.playlistId).select(qPlaylistEntity, qPlayListClaimLineEntity.playlistId.count()).from(qPlaylistEntity)
				.leftJoin(qPlayListClaimLineEntity).on(qPlaylistEntity.playlistId.eq(qPlayListClaimLineEntity.playlistId))
				.leftJoin(qClaimLineEntity).on(qClaimLineEntity.claimLineId.eq(qPlayListClaimLineEntity.claimLineId))
				.where(builder.getValue()).fetch();
	}

	@Override
	public List<Tuple> getAllSharedPlaylists(String loggedInUserId) {
		BooleanBuilder builder = new BooleanBuilder();
		final JPAQuery<SharedPlaylistEntity> query = new JPAQuery<>(entityManager);
		final QSharedPlaylistEntity qSharedPlaylistEntity = QSharedPlaylistEntity.sharedPlaylistEntity;
		final QPlaylistEntity qPlaylistEntity = QPlaylistEntity.playlistEntity;
		final QPlaylistClaimlineEntity qPlayListClaimLineEntity = QPlaylistClaimlineEntity.playlistClaimlineEntity;
		final QClaimLineEntity qClaimLineEntity = QClaimLineEntity.claimLineEntity;

		if (StringUtils.hasText(loggedInUserId)) {
			builder.and(qSharedPlaylistEntity.playlistUserId.eq(loggedInUserId));
		}

		builder.and(qPlaylistEntity.playlistStatus.in(PLAYLIST_STATUS_ACTIVE));
		builder.and(qClaimLineEntity.claimStatusCode.notLike("%CLOSED%")
				.or(qClaimLineEntity.claimStatusCode.isNull()))
				.or(qClaimLineEntity.claimStatusCode.notLike("%WAITING%"));

		List<Tuple> fetch = query.groupBy(qPlaylistEntity.playlistId).select(qPlaylistEntity, qPlayListClaimLineEntity.playlistId.count())
				.from(qSharedPlaylistEntity)
				.leftJoin(qPlaylistEntity).on(qPlaylistEntity.playlistId.eq(qSharedPlaylistEntity.playlistId))
				.leftJoin(qPlayListClaimLineEntity).on(qPlaylistEntity.playlistId.eq(qPlayListClaimLineEntity.playlistId))
				.leftJoin(qClaimLineEntity).on(qClaimLineEntity.claimLineId.eq(qPlayListClaimLineEntity.claimLineId))
				.where(builder.getValue()).fetch();
		return fetch;
	}

}
