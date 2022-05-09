package com.aarete.pi.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Table(name = "Playlist_ClaimLine", indexes = {
		@Index(name = "playlist_claimLine_unique_index", columnList = "PLAYLIST_ID,CLAIMLINE_ID", unique = true) },
		uniqueConstraints = { @UniqueConstraint(name = "playlist_claimLine_uc", columnNames = { "PLAYLIST_ID", "CLAIMLINE_ID" }) })
@Entity
@Data
public class PlaylistClaimlineEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PLAYLIST_CLAIMLINE_ID")
	private long playlistClaimlineId;

	@Column(name = "PLAYLIST_ID")
	private long playlistId;

	@Column(name = "CLAIMLINE_ID")
	private long claimLineId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;

}
