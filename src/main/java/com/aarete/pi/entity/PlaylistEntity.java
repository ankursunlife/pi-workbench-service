package com.aarete.pi.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Table(name = "Playlist", uniqueConstraints = { @UniqueConstraint(name = "playlist_uc", columnNames = { "PLAYLIST_NAME", "PLAYLIST_OWNER_ID", "PLAYLIST_STATUS" }) })
@Entity
@Data
public class PlaylistEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLAYLIST_ID")
	private long playlistId;

	@Column(name = "PLAYLIST_NAME", length = 100)
	private String playlistName;

	@Column(name = "PLAYLIST_DESC" , length = 300)
	private String playlistDesc;

	@Column(name = "PLAYLIST_OWNER_ID")
	private String playlistOwnerId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;

	@Column(name = "PLAYLIST_STATUS")
	private String playlistStatus;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinTable(name = "playlist_claimline_join_table", joinColumns = {
			@JoinColumn(name = "PLAYLIST_ID") }, inverseJoinColumns = { @JoinColumn(name = "PLAYLIST_CLAIMLINE_ID") })
	private List<PlaylistClaimlineEntity> playlistClaimLineEntities = new ArrayList<>();

}