package com.aarete.pi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Bean to provide report's header and its order")
public class CellBean {

	private int cellOrder;
	
	@ApiModelProperty(notes = "Column value", example = "Need to be taken from Enum provided by Backend team")
	private String cellValue;//TODO create ENUM for all columns, most probably column name from the table
	
	@ApiModelProperty(notes = "Column value mapped with ClaimLineBean's property")
	private String cellMapperField;
	
}
