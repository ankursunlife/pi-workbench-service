package com.aarete.pi.bean;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClaimProcessRequest {

		@ApiModelProperty(notes = "Claimline ID on which action needs to be taken.")
		@Size(min = 1)
		private long claimLineId;
	
		private long claimNum;
	
		private int claimLineNum;

		@NotNull
		@ApiModelProperty(notes = "Action that needs to be taken on the list of claimlines.", example = "ASSIGN, ACCEPT, REJECT, PEND, OVERRIDE")
		private String actionTaken;

		@ApiModelProperty(notes = "Don't send. For internal use only.", example = "user@aarete.com")
		private String actionTakenBy;

		@ApiModelProperty(notes = "Send in the case of override", example = "user@aarete.com")
		private String assignedToUser;
		
		@ApiModelProperty(notes = "Send in the case of override", example = "AU1")
		private String assignedToGroupTo;
		
		@NotNull
		@ApiModelProperty(notes = "Claim level ex code information.", example = "CLAIM, CLAIM_LINE")
		private String claimLevelExCode;

		@NotNull
		@ApiModelProperty(notes = "Batch number information.", example = "1")	
		private String batchId;
		
		@ApiModelProperty(notes = "Client unique identifier information.", example = "1")	
		private String clientId;

	    @ApiModelProperty(notes = "Engagement Id is mandatory")
		private String engagementId;
		
		private String comments;
		
		@ApiModelProperty(notes = "When rejected, rejection ID needs to be sent and not text.")
		private int rejectReasonId;
		
		@ApiModelProperty(notes = "This is ExCode level", example = "CLAIM,CLAIM_LINE")
		private String exCodeLevel;
		
		@NotNull
		@ApiModelProperty(notes = "Role based on selected engagement", example = "AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER")
		private String engagementRole;
		
		@ApiModelProperty(notes = "ExCode level bean list")
		private List<ExCodeBean> exCodeBeanList;
		
		public ClaimProcessRequest(long claimLineId, long claimNum, int claimLineNum,String actionTaken,String engagementRole, String exCodeLevel, String engagementId) {
			this.claimLineId=claimLineId;
			this.claimNum=claimNum;
			this.claimLineNum=claimLineNum;
			this.actionTaken=actionTaken;
			this.engagementRole=engagementRole;
			this.exCodeLevel=exCodeLevel;
			this.engagementId=engagementId;
		}
}