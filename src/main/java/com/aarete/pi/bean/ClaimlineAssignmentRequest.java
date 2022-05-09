package com.aarete.pi.bean;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Bean to send claim assignment details")
public class ClaimlineAssignmentRequest {

	@ApiModelProperty(notes = "Email Ids of the users to be assigned")
	private List<String> userIdList;
	
	@ApiModelProperty(notes = "Claim line Ids of the user to be assigned, different from claimline number")
	private List<Long> claimLineIdList;
	
	@ApiModelProperty(notes = "Level to which claim line will be assigned.", example = "ONE,TWO,THREE")
	private String approverLevel;
}
