package com.aarete.pi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Bean to add or list user")
public class User {

	@ApiModelProperty(notes = "User Email Id", example = "sample@aarete.com")
	private String userId;

	@ApiModelProperty(notes = "User Name", example = "Tom Hanks")
	private String userName;

	@ApiModelProperty(notes = "Role based on selected engagement", example = "AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER")
	private String userType;

	@ApiModelProperty(notes = "User image url", example = "")
	private String imageUrl;
	
	@ApiModelProperty(notes = "User Status", example = "ACTIVE,INACTIVE")
	private String status;
	
}
