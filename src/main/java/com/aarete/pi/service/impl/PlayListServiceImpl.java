package com.aarete.pi.service.impl;

import static com.aarete.pi.constant.WorkbenchConstants.PLAYLIST_STATUS_ACTIVE;
import static com.aarete.pi.helper.ClaimServiceHelper.addExCodeDetails;
import static com.aarete.pi.helper.PlayListServiceHelper.*;
import static com.aarete.pi.helper.SecurityHelper.getUserDetailsBean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.aarete.pi.dao.MasterTableDAO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.aarete.pi.bean.AssignClaimRequest;
import com.aarete.pi.bean.ClaimLineBean;
import com.aarete.pi.bean.ClaimProcessRequest;
import com.aarete.pi.bean.Playlist;
import com.aarete.pi.bean.PlaylistResponse;
import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.dao.ClaimDAO;
import com.aarete.pi.dao.PlaylistDAO;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.PlaylistClaimlineEntity;
import com.aarete.pi.entity.PlaylistEntity;
import com.aarete.pi.entity.SharedPlaylistEntity;
import com.aarete.pi.enums.ClaimAction;
import com.aarete.pi.enums.ClaimEditType;
import com.aarete.pi.enums.ClaimStatus;
import com.aarete.pi.enums.ExCodeLevel;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.repository.PlaylistRepository;
import com.aarete.pi.repository.SharedPlaylistRepository;
import com.aarete.pi.service.PlayListService;
import com.querydsl.core.Tuple;

@Service
public class PlayListServiceImpl implements PlayListService {
	
	@Autowired
    private PlaylistRepository playlistRepository;

	@Autowired
	private SharedPlaylistRepository sharedPlaylistRepository;

	@Autowired
	private PlaylistDAO playlistDAO;
	
	@Autowired
	private ClaimDAO claimDAO;

	@Autowired
	private MasterTableDAO masterTableDAO;
	
	@Override
	@Transactional
	public PlaylistEntity addPlayList(Playlist playlist) throws Exception {
		PlaylistEntity playlistEntity = new PlaylistEntity();
		BeanUtils.copyProperties(playlist, playlistEntity);
		playlistEntity.setPlaylistStatus(PLAYLIST_STATUS_ACTIVE);
		playlistEntity.setCreatedTime(Timestamp.from(Instant.now()));
		playlistEntity.setCreatedBy(playlist.getLoggedInUser());
		PlaylistEntity savedPlaylistEntity = playlistRepository.save(playlistEntity);
		if(ClaimStatus.GROUP_QUEUE.name().equals(playlist.getQueueType())) {
			AssignClaimRequest ClaimRequest = new AssignClaimRequest();
			List<ClaimProcessRequest> claims = new ArrayList<>();
			playlist.getClaimLineIdsToAdd().stream().forEach(claimLineId -> {
				ClaimLineEntity fetch = claimDAO.getClaimLine(claimLineId);
				if (ClaimEditType.MULTI_LINE.toString().equals(fetch.getEditType())) {
					List<Long> claimIds = claimDAO.getClaimIds(fetch.getClaimNum());
					for (Long existingIds : playlist.getClaimLineIdsToAdd()) {
						if (existingIds != claimLineId)
							claimIds.add(existingIds);
					}
					playlist.setClaimLineIdsToAdd(claimIds);
					claims.add(new ClaimProcessRequest(claimLineId,fetch.getClaimNum(),fetch.getClaimLineNum(),ClaimAction.ASSIGN.name(), playlist.getEngagementRole(), ExCodeLevel.CLAIM.name(), fetch.getEngagementId()));
				} else {
					claims.add(new ClaimProcessRequest(claimLineId,fetch.getClaimNum(),fetch.getClaimLineNum(),ClaimAction.ASSIGN.name(), playlist.getEngagementRole(), ExCodeLevel.CLAIM_LINE.name() ,fetch.getEngagementId()));
				}
			});
			ClaimRequest.setClaimProcessList(claims);
			UserDetailsBean userDetailsBean = getUserDetailsBean();
			claimDAO.processClaimLines(ClaimRequest, userDetailsBean);
		}
		else
		{
			playlist.getClaimLineIdsToAdd().stream().forEach(claimLineId -> {
				ClaimLineEntity fetch = claimDAO.getClaimLine(claimLineId);
				if (ClaimEditType.MULTI_LINE.toString().equals(fetch.getEditType())) {
					List<Long> claimIds = claimDAO.getClaimIds(fetch.getClaimNum());
					for (Long existingIds : playlist.getClaimLineIdsToAdd()) {
						if (existingIds != claimLineId)
							claimIds.add(existingIds);
					}
					playlist.setClaimLineIdsToAdd(claimIds);
				}
			});
		}
		if(!CollectionUtils.isEmpty(playlist.getClaimLineIdsToAdd())) {
			List<PlaylistClaimlineEntity> playlistClaimLineEntities = playlist.getClaimLineIdsToAdd().stream().map(claimLineId -> {
				PlaylistClaimlineEntity playlistClaimlineEntity = new PlaylistClaimlineEntity();
				playlistClaimlineEntity.setPlaylistId(savedPlaylistEntity.getPlaylistId());
				playlistClaimlineEntity.setClaimLineId(claimLineId);
				playlistClaimlineEntity.setCreatedTime(Timestamp.from(Instant.now()));
				playlistClaimlineEntity.setCreatedBy(playlist.getLoggedInUser());
				return playlistClaimlineEntity;
			}).collect(Collectors.toList());
			savedPlaylistEntity.setPlaylistClaimLineEntities(playlistClaimLineEntities);
		}
    	return playlistRepository.save(savedPlaylistEntity);
    }

    @Override
    public PlaylistResponse getAllMyPlayList(String loggedInUserId) {
		return getPlaylistAndCount(playlistDAO.getAllPlaylists(loggedInUserId));
    }

    @Override
	@Transactional
    public void updatePlayList(Playlist playlist) throws Exception {
		List<PlaylistEntity> playlistEntities = new ArrayList<>();
		// line num - {claim num - [line num]}
		Map<Long, Map<Long, List<Long>>> claimMap = new HashMap<>();
		UserDetailsBean userDetailsBean = getUserDetailsBean();

		for (long playlistId : playlist.getPlaylistIds()) {
			Optional<PlaylistEntity> playlistEntityOptional = playlistRepository.findById(playlistId);
			if(playlistEntityOptional.isPresent()) {
				PlaylistEntity savedPlaylistEntity = playlistEntityOptional.get();
				List<PlaylistClaimlineEntity> savedPlaylistClaimLineEntities = savedPlaylistEntity.getPlaylistClaimLineEntities();
				List<Long> savedClaimLineIds = savedPlaylistClaimLineEntities.stream().map(PlaylistClaimlineEntity::getClaimLineId).collect(Collectors.toList());

				AssignClaimRequest ClaimRequest = new AssignClaimRequest();
				List<ClaimProcessRequest> claimRequests = new ArrayList<>();
				boolean isNotDuplicate = false;

				if (!CollectionUtils.isEmpty(playlist.getClaimLineIdsToRemove())) {
					// remove claims from playlist
					List<Long> allClaimLinesToRemove = getAllClaimLinesToRemove(playlist.getClaimLineIdsToRemove(), claimDAO);

					List<PlaylistClaimlineEntity> collect = savedPlaylistClaimLineEntities.stream()
							.filter(entity -> !allClaimLinesToRemove.contains(entity.getClaimLineId()))
							.collect(Collectors.toList());

					savedPlaylistClaimLineEntities.retainAll(collect);
				} else {
					// adding claims to playlist
					buildClaimMap(playlist.getClaimLineIdsToAdd(), claimMap, claimDAO);
					for (Map.Entry<Long, Map<Long, List<Long>>> longMapEntry : claimMap.entrySet()) {
						if (isNotDuplicate(longMapEntry.getKey(), playlist, savedClaimLineIds, claimMap)) {
							isNotDuplicate = true;
							createPlaylistClaimLineEntities(playlist, savedPlaylistEntity, savedPlaylistClaimLineEntities,
									claimRequests, longMapEntry.getKey(), claimDAO, claimMap, savedClaimLineIds);
						}
					}
					if (isNotDuplicate || CollectionUtils.isEmpty(playlist.getClaimLineIdsToAdd())) {
						// update playlist name, desc, user, time.
						setPlaylistValues(playlist, savedPlaylistEntity);
					}

					// Move claims from group queue to my queue for GROUP_QUEUE
					if (ClaimStatus.GROUP_QUEUE.name().equals(playlist.getQueueType())) {
						ClaimRequest.setClaimProcessList(claimRequests);
						claimDAO.processClaimLines(ClaimRequest, userDetailsBean);
					}
				}
				savedPlaylistEntity.setPlaylistClaimLineEntities(savedPlaylistClaimLineEntities);
				playlistEntities.add(savedPlaylistEntity);
				playlistRepository.saveAll(playlistEntities);
			}
		}
    }

	@Override
	@Transactional
    public void deletePlayList(long playlistId) {
		playlistDAO.deletePlayList(playlistId);
    }

	@Override
	public PlaylistResponse getPlayList(long playlistId) throws RecordNotFound {
		PlaylistResponse playlistResponse = new PlaylistResponse();
		List<Tuple> fetch = playlistDAO.getPlaylist(playlistId);
		Set<String> providerNames = new HashSet<>();

		List<ClaimLineBean> claimLineBeans = new ArrayList<>();
		BigDecimal lineOpportunityAmount = BigDecimal.ZERO;
		int exCodeCount = 0;

		for (Tuple tuple : fetch) {
			Object[] objects = tuple.toArray();
			ClaimLineEntity claimLineEntity = (ClaimLineEntity) objects[0];
			PlaylistEntity playlistEntity = (PlaylistEntity) objects[1];
			playlistResponse.setUpdatedTime(playlistEntity.getUpdatedTime());
			playlistResponse.setCreatedTime(playlistEntity.getCreatedTime());
			ClaimLineBean claimLineBean = new ClaimLineBean();
			BeanUtils.copyProperties(claimLineEntity, claimLineBean);
			addExCodeDetails(claimLineEntity, claimLineBean);
			lineOpportunityAmount = lineOpportunityAmount.add(claimLineEntity.getLineOpportunityAmount());
			exCodeCount += claimLineEntity.getExCodeBeanList().size();
			claimLineBeans.add(claimLineBean);
			providerNames.add(claimLineBean.getBillingProviderFullName());
		}
		playlistResponse.setClaimCount(fetch.size());
		playlistResponse.setPlaylistOpportunityAmount(lineOpportunityAmount);
		playlistResponse.setPlaylistExCodes(exCodeCount);
		playlistResponse.setPlaylistProviderCount(providerNames.size());
		playlistResponse.setClaimLines(claimLineBeans);
		return playlistResponse;
	}

	@Override
	public void saveSharedPlayList(@Valid Playlist playlistRequest) {
		List<String> duplicateUserIds = new ArrayList<>();
		for(String userId : playlistRequest.getSharedUserIdList()) {
			SharedPlaylistEntity sharedPlaylistEntity = new SharedPlaylistEntity();
			sharedPlaylistEntity.setPlaylistId(playlistRequest.getPlaylistId());
			sharedPlaylistEntity.setCreatedTime(Timestamp.from(Instant.now()));
			sharedPlaylistEntity.setPlaylistUserId(userId);
			try {
				sharedPlaylistRepository.save(sharedPlaylistEntity);
			} catch (Exception e) {
				// ignore duplicate sharing playlist
				duplicateUserIds.add(userId);
			}
		}

		if(!CollectionUtils.isEmpty(duplicateUserIds)) {
			playlistRequest.getDuplicateUserNames().addAll(masterTableDAO.getUserNames(duplicateUserIds));
		}
	}

	@Override
	public PlaylistResponse getAllSharedPlayList(String loggedInUserId) throws RecordNotFound {
		return getPlaylistAndCount(playlistDAO.getAllSharedPlaylists(loggedInUserId));
	}

}
