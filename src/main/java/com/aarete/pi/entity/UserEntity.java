package com.aarete.pi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "User_Details")
public class UserEntity {

    @Id
    @Column(name = "EMAIL_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    //e.g. CLIENT,AARETE
    @Column(name = "USER_TYPE")
    private String userType;
    
    @Column(name = "IMAGE_URL",length = 1000)
    private String imageUrl;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CREATED_TIME")
    private Timestamp createdTime;

    @Column(name = "UPDATED_TIME")
    private Timestamp updatedTime;
    
    //e.g. ACTIVE,INACTIVE
    @Column(name = "STATUS")
    private String status;
    
}
