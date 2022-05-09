package com.aarete.pi.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Claimline_Comment")
public class ClaimlineCommentEntity {

    @Id
    @Column(name = "CLAIMLINE_COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long claimlineCommentId;

    @Column(name = "COMMENT", length = 1000)
    private String comment;

    @Column(name = "COMMENT_TIME")
    private Timestamp commentTime;

    @Column(name = "USER_ID")
    private String userId;
    
    @Column(name = "CLAIMLINE_ID")
	private Long claimLineId;//TODO foreign key from Claimline table

    @Column(name = "COMMENTED_BY_TYPE")
    private String commentedByType;//TODO Check if comment is added by AArete or Client person
    
}
