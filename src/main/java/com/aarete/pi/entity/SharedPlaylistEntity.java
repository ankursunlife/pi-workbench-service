package com.aarete.pi.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "Shared_Playlist",
        uniqueConstraints = { @UniqueConstraint(name = "shared_playlist_uc", columnNames = { "PLAYLIST_ID", "PLAYLIST_USER_ID" }) })
@Data
public class SharedPlaylistEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHARED_PLAYLIST_ID")
    private long sharedPlaylistId;
	
	@Column(name = "PLAYLIST_ID")
    private long playlistId;
	
	@Column(name = "PLAYLIST_USER_ID")
    private String playlistUserId;
	
	@Column(name = "CREATED_BY")
    private String createdBy;
    
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    
    @Column(name = "CREATED_TIME")
    private Timestamp createdTime;
    
    @Column(name = "UPDATED_TIME")
    private Timestamp updatedTime;

}
