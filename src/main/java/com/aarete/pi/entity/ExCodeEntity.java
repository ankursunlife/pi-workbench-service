package com.aarete.pi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Ex_Code")
public class ExCodeEntity {

    @Id
    @Column(name = "EX_CODE_ID")
    private String id;

    @Column(name = "EX_CODE_NAME")
    private String name;

    @Column(name = "EX_CODE_DESC")
    private String desc;

}
