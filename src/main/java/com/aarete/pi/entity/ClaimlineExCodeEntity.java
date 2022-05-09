package com.aarete.pi.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Audited
@Table(name = "Claimline_Excode")
public class ClaimlineExCodeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLAIMLINE_EXCODE_ID")
	private Long claimlineExCodeId;

	@Column(name = "EX_CODE_ID")
	private String exCodeId; //FOREGIN KEY TO EXCODE

	@Column(name = "PILLAR")
	private String pillar;

	@Column(name = "source")
	private String source;

	@Column(name = "BRIEF")
	private String brief;

	@Column(name = "LINE_OPPORTUNITY_AMOUNT")
	private BigDecimal lineOpportunityAmount;

	@Column(name = "APPROVER_ONE_ACTION")
	private String approverOneAction;

	@Column(name = "APPROVER_TWO_ACTION")
	private String approverTwoAction;

	@Column(name = "APPROVER_THREE_ACTION")
	private String approverThreeAction;

	@Column(name = "APPROVER_FOUR_ACTION")
	private String approverFourAction;

	@Column(name = "CONFIDENCE_SCORE")
	private float confidenceScore;
	
	@Column(name = "CLAIM_NUM")
	private long claimNum;

	@Column(name = "CLAIM_LINE_NUM")
	private int claimLineNum;
	
	@Column(name = "EX_CODE_LEVEL")
	private String exCodeLevel;
	
	@Column(name = "BATCH_ID")
	private long batchId;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "CREATED_TIME")
	private Timestamp createdTime;

	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;

	@Column(name = "EX_CODE_TYPE")
	private String exCodeType;

	@Column(name = "PRIORITIZATION_SCORE")
	private BigDecimal prioritizationScore;

	@Column(name = "ORDER_RATING")
	private String orderRating;

	@Column(name = "EVIDENCE_CLAIM_NUM")
	private long evidenceClaimNum;

	@Column(name = "EVIDENCE_CLAIM_LINE_NUM")
	private int evidenceClaimLineNum;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CLAIMLINE_ID")
	private ClaimLineEntity claimLineEntity;

}
