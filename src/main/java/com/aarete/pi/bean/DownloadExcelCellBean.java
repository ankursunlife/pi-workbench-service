package com.aarete.pi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Bean to provide report's header and its order")
public class DownloadExcelCellBean {

	private int cellOrder;
	
	@ApiModelProperty(notes = "Column value")
	private String cellValue;
	@ApiModelProperty(notes = "Column Header")
	private String cellHeader;
	@ApiModelProperty(notes = "Column value mapped with ClaimLineBean's property")
	private String cellMapperField;
}
