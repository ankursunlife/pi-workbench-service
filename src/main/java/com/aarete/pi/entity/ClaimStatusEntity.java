package com.aarete.pi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Id;

@Table(name = "Claim_Status")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimStatusEntity {

    @Id
    @Column(name = "CLAIM_STATUS_CD")
    private String claimStatusCode;

    @Column(name = "CLAIM_STATUS_NAME")
    private String claimStatusName;

    @Column(name = "CLAIM_STATUS_DESC")
    private String claimStatusDesc;
    
    //MY_QUEUE = MY_QUEUE, CLOSED = CLOSED, PEND = PEND, WAITING = WAITING

}