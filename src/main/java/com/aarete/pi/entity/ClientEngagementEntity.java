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
@Table(name = "Client_Engagement")
public class ClientEngagementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "ENGAGEMENT_ID")
    private String engagementId;
    
    @Column(name = "CLIENT_NAME")
    private String clientName;

    @Column(name = "ENGAGEMENT_NAME")
    private String engagementName;
    
    @Column(name = "CREATED_TIME")
    private Timestamp createdTime;
    
    @Column(name = "UPDATED_TIME")
    private Timestamp updatedTime;

}
