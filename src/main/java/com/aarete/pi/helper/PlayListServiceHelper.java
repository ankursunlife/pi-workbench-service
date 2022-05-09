package com.aarete.pi.helper;

import com.aarete.pi.bean.ClaimProcessRequest;
import com.aarete.pi.bean.Playlist;
import com.aarete.pi.bean.PlaylistResponse;
import com.aarete.pi.dao.ClaimDAO;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.PlaylistClaimlineEntity;
import com.aarete.pi.entity.PlaylistEntity;
import com.aarete.pi.enums.ClaimAction;
import com.aarete.pi.enums.ClaimEditType;
import com.aarete.pi.enums.ClaimStatus;
import com.aarete.pi.enums.ExCodeLevel;
import com.aarete.pi.enums.Role;
import com.querydsl.core.Tuple;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aarete.pi.constant.WorkbenchConstants.CLAIM_LINE_MSG_FORMAT;

public class PlayListServiceHelper {

    public static PlaylistResponse getPlaylistAndCount(List<Tuple> tuples) {
        List<Playlist> playlists = new ArrayList<>();
        PlaylistResponse playlistResponse = new PlaylistResponse();
        int totalClaimCount = 0;
        for (Tuple tuple : tuples) {
            Object[] objects = tuple.toArray();
            PlaylistEntity playlistEntity = (PlaylistEntity) objects[0];
            Playlist playlist = new Playlist();
            BeanUtils.copyProperties(playlistEntity, playlist);
            int claimCount = ((Long) objects[1]).intValue();
            playlist.setClaimCount(claimCount);
            totalClaimCount += claimCount;
            playlists.add(playlist);
        }
        playlistResponse.setPlayLists(playlists);
        playlistResponse.setClaimCount(totalClaimCount);
        return playlistResponse;
    }

    public static boolean isNotDuplicate(long claimLineId, Playlist playlist, List<Long> savedClaimLineIds, Map<Long, Map<Long, List<Long>>> claimMap) {
        if(savedClaimLineIds.contains(claimLineId)) {
            List<String> duplicateClaimLineIds = playlist.getDuplicateClaimLineIds();
            Map<Long, List<Long>> longListMap = claimMap.get(claimLineId);

            for (Map.Entry<Long, List<Long>> longListEntry : longListMap.entrySet()) {
                long claimNum = longListEntry.getKey();
                List<Long> claimLines = longListEntry.getValue();
                for (Long claimLine : claimLines) {
                    duplicateClaimLineIds.add(claimNum+ CLAIM_LINE_MSG_FORMAT +claimLine);
                }
            }
            playlist.setDuplicateClaimLineIds(duplicateClaimLineIds);
            return false;
        }
        return true;
    }

    public static void setPlaylistValues(Playlist playlist, PlaylistEntity savedPlaylistEntity) {
        if(StringUtils.hasText(playlist.getPlaylistName())) {
            savedPlaylistEntity.setPlaylistName(playlist.getPlaylistName());
        }
        if(StringUtils.hasText(playlist.getPlaylistDesc())) {
            savedPlaylistEntity.setPlaylistDesc(playlist.getPlaylistDesc());
        }
        savedPlaylistEntity.setUpdatedBy(playlist.getLoggedInUser());
        savedPlaylistEntity.setUpdatedTime(Timestamp.from(Instant.now()));
    }

    public static void buildClaimMap(List<Long> idList, Map<Long, Map<Long, List<Long>>> claimMap, final ClaimDAO claimDAO) {
        idList.stream().forEach(claimLineId -> {
            List<Long> claimIds;
            Map<Long, List<Long>> multiLineMap = new HashMap<>();
            ClaimLineEntity fetch = claimDAO.getClaimLine(claimLineId);
            if(fetch != null && ClaimEditType.MULTI_LINE.toString().equals(fetch.getEditType())) {
                claimIds = claimDAO.getClaimIds(fetch.getClaimNum());
            } else {
                claimIds = Arrays.asList(claimLineId);
            }
            multiLineMap.put(fetch.getClaimNum(), claimIds);
            claimMap.put(claimLineId, multiLineMap);
        });
    }

    public static List<Long> getAllClaimLinesToRemove(List<Long> idList, final ClaimDAO claimDAO) {
        List<Long> claimLinesToRemove = new ArrayList<>();
        idList.stream().forEach(claimLineId -> {
            ClaimLineEntity fetch = claimDAO.getClaimLine(claimLineId);
            if(fetch != null && ClaimEditType.MULTI_LINE.toString().equals(fetch.getEditType())) {
                claimLinesToRemove.addAll(claimDAO.getClaimIds(fetch.getClaimNum()));
            } else {
                claimLinesToRemove.add(claimLineId);
            }
        });
        return claimLinesToRemove;
    }

    public static void createPlaylistClaimLineEntities(Playlist playlist, PlaylistEntity savedPlaylistEntity, List<PlaylistClaimlineEntity> savedPlaylistClaimLineEntities,
                                                       List<ClaimProcessRequest> claimRequests, Long claimLineId, final ClaimDAO claimDAO, Map<Long, Map<Long,
            List<Long>>> claimMap, List<Long> savedClaimLineIds) {
        claimMap.get(claimLineId).entrySet().stream()
                .flatMap(map -> map.getValue().stream()).filter(lineId -> !savedClaimLineIds.contains(lineId)).forEach(lineId -> {
            PlaylistClaimlineEntity playlistClaimlineEntity = new PlaylistClaimlineEntity();
            playlistClaimlineEntity.setPlaylistId(savedPlaylistEntity.getPlaylistId());
            playlistClaimlineEntity.setClaimLineId(lineId);
            playlistClaimlineEntity.setCreatedTime(Timestamp.from(Instant.now()));
            playlistClaimlineEntity.setCreatedBy(playlist.getLoggedInUser());
            if (ClaimStatus.GROUP_QUEUE.name().equals(playlist.getQueueType())) {
                // TODO this DB call need to be removed, only extra ClaimLineNum is required
                ClaimLineEntity fetch = claimDAO.getClaimLine(lineId);
                claimRequests.add(new ClaimProcessRequest(lineId, fetch.getClaimNum(), fetch.getClaimLineNum(), ClaimAction.ASSIGN.name(), Role.AARETE_USER.name(), ExCodeLevel.CLAIM_LINE.name(), fetch.getEngagementId()));
            }
            savedPlaylistClaimLineEntities.add(playlistClaimlineEntity);
        });
    }
}
