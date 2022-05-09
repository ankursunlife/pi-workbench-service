package com.aarete.pi.dao;

import java.util.List;

import javax.validation.Valid;

import com.aarete.pi.bean.Playlist;
import com.aarete.pi.exception.RecordNotFound;
import com.querydsl.core.Tuple;

public interface PlaylistDAO {

	void addPlaylist(Playlist playlistRequest);

	List<Playlist> getAllMyPlayList();

	void updatePlayList(Playlist playlistRequest);

	long deletePlayList(long playlistId);

	List<Tuple> getPlaylist(long playlistId) throws RecordNotFound;

	void sharePlayList(@Valid Playlist playlistRequest);

	List<Tuple> getAllPlaylists(String loggedInUserId);

	List<Tuple> getAllSharedPlaylists(String loggedInUserId) throws RecordNotFound;

}
