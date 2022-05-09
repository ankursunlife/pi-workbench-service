package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MyFilterRequestBean {

	@ApiModelProperty(notes = "Send selected values only", example = "CPT, DIAGNOSIS, REVENUE")
	@NotEmpty(message = "Filter name is mandatory.")
	private String filterName;
	
	private EngagementFilterRequest engagementFilters;
	
}
