package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserRequest {

	@ApiModelProperty(notes = "Engagement Id", example = "MHP")
	private String engagementId;
	
	@ApiModelProperty(notes = "Role based on selected engagement", example = "AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER")
	private String engagementRole;
	
}
