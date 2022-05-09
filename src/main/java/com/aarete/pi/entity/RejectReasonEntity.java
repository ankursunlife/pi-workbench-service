package com.aarete.pi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;

@Table(name = "Reject_Reason")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectReasonEntity {

    @Id
    @Column(name = "REJECT_REASON_ID")
    private Integer id;

    @Column(name = "REJECT_REASON_NAME")
    private String name;

    @Column(name = "REJECT_REASON_DESC")
    private String desc;

}