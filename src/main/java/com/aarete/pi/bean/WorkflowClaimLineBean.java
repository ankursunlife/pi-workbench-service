package com.aarete.pi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "This bean will be sent when claim line list is called as well as claim line details are called.")
public class WorkflowClaimLineBean {

	@ApiModelProperty(notes = "Autogenerated by DB")
	private long claimLineId;

	private long claimNum;

	private int claimLineNum;

	@ApiModelProperty(notes = "Will be marked as CLOSED once workflow is completed and wont be availble on MyQueue page. Will not have typical status like PEND, WAITING", example = "CLOSED")
	private String claimStatusCode;

	private List<ExCodeBean> exCodeBeanList;

	private String exCode;
	
	@ApiModelProperty(notes = "This is ExCode level", example = "CLAIM,CLAIM_LINE")
	private String exCodeLevel;

}