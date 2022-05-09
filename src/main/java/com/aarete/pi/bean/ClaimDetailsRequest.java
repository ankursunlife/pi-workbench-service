package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClaimDetailsRequest {
	
	@ApiModelProperty(required = true, notes = "Engagement Id for a client")
	private String engagementId;
	
	@ApiModelProperty(required = true, notes = "Claim Number")
	private long claimNum;
	
	@ApiModelProperty(notes = "Query Execution Id, not required for the first page of results")
	private String queryExecutionId;
	
	@ApiModelProperty(notes = "Next Token value, not required for the first page of results")
	private String nextToken;
}
