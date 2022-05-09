package com.aarete.pi.bean;

import lombok.Data;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;

@Data
public class CodeType {

	@ApiModelProperty(notes = "", example = "CPT, DIAGNOSIS, REVENUE")
	private String code;
	
	@ApiModelProperty(notes = "List of values user selects for given code", example = "")
	private List<String> codeValues;
	
}
