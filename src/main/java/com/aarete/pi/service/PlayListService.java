package com.aarete.pi.service;

import java.sql.SQLException;

import javax.validation.Valid;

import com.aarete.pi.bean.Playlist;
import com.aarete.pi.bean.PlaylistResponse;
import com.aarete.pi.entity.PlaylistEntity;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.exception.WorkbenchException;

public interface PlayListService {

	PlaylistEntity addPlayList(Playlist playlistRequest) throws SQLException, WorkbenchException, Exception;

    PlaylistResponse getPlayList(long playlistId) throws RecordNotFound;

    PlaylistResponse getAllMyPlayList(String loggedInUserId);

    void updatePlayList(Playlist playlistRequest) throws Exception;

    void deletePlayList(long playlistId);

	void saveSharedPlayList(@Valid Playlist playlistRequest);

    PlaylistResponse getAllSharedPlayList(String loggedInUserId) throws RecordNotFound;
}
