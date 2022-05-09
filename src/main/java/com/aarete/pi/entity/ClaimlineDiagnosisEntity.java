package com.aarete.pi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "Claimline_Diagnosis", indexes = {
		@Index(name = "claimline_diagnosis_unique_index", columnList = "CLAIMLINE_ID,DIAG_CD_1,POA", unique = true) })
@Data
public class ClaimlineDiagnosisEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLAIMLINE_DIAGNOSIS_ID")
	private long claimlineDiagnosisId;

	@Column(name = "DIAG_CD_1")
	private String dxCode;

	@Column(name = "POA")
	private String poa;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CLAIM_STATUS_CD")
	private String claimStatusCode;
	
	@Column(name = "CLAIM_NUM")
	private long claimNum;

	@Column(name = "CLAIM_LINE_NUM")
	private int claimLineNum;
	
	@Column(name = "BATCH_ID")
	private long batchId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CLAIMLINE_ID")
	private ClaimLineEntity claimLineEntity;

}
