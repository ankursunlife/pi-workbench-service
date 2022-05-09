package com.aarete.pi.bean;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExCodeBean {

	private String exCodeId;//e.g. DUP,PTP
	
	private String exCodeName;//e.g. DUP,PTP
	
	private String pillar;//should be populate from Pillar table 
	
	private String source;
	
	private float confidenceScore;//Need to calculate
	
	private String brief;
	
	private BigDecimal lineOpportunityAmount;

	private String approverOneAction;

	private String approverTwoAction;

	private String approverThreeAction;

	private String approverFourAction;
	
	//TODO add opportunity and evidence claim details
	private long opportunityClaimNum;

	@ApiModelProperty(notes = "CLAIM, CLAIM_LINE")
	private String exCodeType;

	@ApiModelProperty(notes = "CLAIM, CLAIM_LINE")
	private String claimExCodeLevel;

	@ApiModelProperty(notes = "Multiplication of confidence score and opportunity amount")
	private String prioritizationScore;

	private String orderRating;

	private long evidenceClaimNum;

	private int evidenceClaimLineNum;

}
