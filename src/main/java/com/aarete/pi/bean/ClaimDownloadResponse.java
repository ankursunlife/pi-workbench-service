package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClaimDownloadResponse {
	
	@ApiModelProperty
	private String status;
	
	@ApiModelProperty
	private String message;
	
	@ApiModelProperty
	private String location;

}
