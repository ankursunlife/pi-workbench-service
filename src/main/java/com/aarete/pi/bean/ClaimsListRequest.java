package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClaimsListRequest {

	@ApiModelProperty(required = true, notes = "Engagement Id for a client")
	private String engagementId;
	
	@ApiModelProperty(notes = "Member Id")
	private String memberId;
	
	@ApiModelProperty(notes = "Provider Number")
	private String providerNumber;
	
	@ApiModelProperty(notes = "Time period in days")
	private int timePeriod;
	
	@ApiModelProperty(notes = "Pillar Id")
	private String pillarId;
	
	@ApiModelProperty(notes = "Product Line of Business Id")
	private String lobId;
	
	@ApiModelProperty(notes = "Edit Flag")
	private String editType;
	
	@ApiModelProperty(notes = "Type")
	private String type;
	
	@ApiModelProperty(notes = "Query Execution Id, not required for the first page of results")
	private String queryExecutionId;
	
	@ApiModelProperty(notes = "Next Token value, not required for the first page of results")
	private String nextToken;
}
