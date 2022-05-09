package com.aarete.pi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Class representing a claim status.")
public class ClaimStatusRequest {

	@ApiModelProperty(required = true)
	private long claimLineId;

	@ApiModelProperty(required = true)
	private long claimNum;

	@ApiModelProperty(required = true)
	private int claimLineNum;

	@ApiModelProperty(required = true)
	private String engagementId;

	@ApiModelProperty(required = true)
	private String claimStatusCode;

}
