package com.aarete.pi.controller;

import static com.aarete.pi.helper.SecurityHelper.getUserDetailsBean;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.aarete.pi.bean.Playlist;
import com.aarete.pi.bean.PlaylistResponse;
import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.entity.PlaylistEntity;
import com.aarete.pi.exception.AccessDenied;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.service.PlayListService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playlist")
@Api(tags = "Playlist Management", value = "PlaylistManagement")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PlayListController {

	@Autowired
	private PlayListService playListService;

	/**
	 * @param playlistRequest
	 * @return
	 * @throws Exception 
	 */
	@PostMapping(value = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "API to Create playlist", notes = "This API will create playlist, also if claimline IDs are sent, those will be added while creating playlist.")
	public ResponseEntity<PlaylistEntity> createPlayList(@Valid @RequestBody Playlist playlistRequest) throws Exception {
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		playlistRequest.setPlaylistOwnerId(userDetailsBean.getUserId());
		playlistRequest.setLoggedInUser(userDetailsBean.getUserId());
		PlaylistEntity playlistEntity = playListService.addPlayList(playlistRequest);
		return new ResponseEntity<>(playlistEntity, HttpStatus.OK);
	}


	/**
	 * @return
	 * @throws AccessDenied
	 */
	@GetMapping("/my-playlist")
	@ApiOperation(value = "API to get all playlists of the user.")
	public ResponseEntity<PlaylistResponse> getAllMyPlayList() throws AccessDenied {
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		PlaylistResponse playlistResponse = playListService.getAllMyPlayList(userDetailsBean.getUserId());
		return new ResponseEntity<>(playlistResponse, HttpStatus.OK);
	}

	
	/**
	 * @param playlistId
	 * @return
	 * @throws RecordNotFound
	 */
	@GetMapping("/{playlistId}")
	@ApiOperation(value = "This API will provide all the details of the Playlist.")
	public ResponseEntity<PlaylistResponse> getPlayListDetails(@PathVariable("playlistId") long playlistId) throws RecordNotFound {
		PlaylistResponse playlistResponse = playListService.getPlayList(playlistId);
		return new ResponseEntity<>(playlistResponse, HttpStatus.OK);
	}

	/**
	 * @param playlistRequest
	 * @return
	 * @throws AccessDenied
	 */
	@PutMapping("")
	@ApiOperation(value = "API to update the playlist. This API will add new claimlines in the given playlist.")
	public ResponseEntity<Playlist> updatePlayList(@Valid @RequestBody Playlist playlistRequest) throws Exception {
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		playlistRequest.setLoggedInUser(userDetailsBean.getUserId());
		playListService.updatePlayList(playlistRequest);
		return new ResponseEntity<>(playlistRequest, HttpStatus.OK);
	}

	/**
	 * @param playlistId
	 * @return
	 */
	@DeleteMapping("/{playlistId}")
	@ApiOperation(value = "API to delete the playlist.",notes = "This API is used to delete the playlist")
	public ResponseEntity<Void> deletePlayList(@PathVariable("playlistId") long playlistId) {
		playListService.deletePlayList(playlistId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	
	/**
	 * @param playlistRequest
	 * @return
	 * @throws AccessDenied
	 */
	@PostMapping(value = "/share", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Share playlist", notes = "This API is used to share playlist with multiple users.")
	public ResponseEntity<Playlist> sharePlayList(@Valid @RequestBody Playlist playlistRequest) throws AccessDenied {
		playListService.saveSharedPlayList(playlistRequest);
		return new ResponseEntity<>(playlistRequest, HttpStatus.OK);
	}

	
	/**
	 * @return
	 * @throws AccessDenied
	 * @throws RecordNotFound
	 */
	@GetMapping("/shared-playlist")
	@ApiOperation(value = "API to get all playlist shared with user")
	public ResponseEntity<PlaylistResponse> getAllSharedPlayList() throws AccessDenied, RecordNotFound {
		PlaylistResponse playlistResponse;
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		String loggedInUserId = userDetailsBean.getUserId();
		playlistResponse = playListService.getAllSharedPlayList(loggedInUserId);
		return new ResponseEntity<>(playlistResponse, HttpStatus.OK);
	}
}
