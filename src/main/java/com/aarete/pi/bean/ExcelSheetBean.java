package com.aarete.pi.bean;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Bean to send claim assignment details")
public class ExcelSheetBean {

	@ApiModelProperty(notes = "Sheet name")
	private String sheetName;
	
	@ApiModelProperty(notes = "Cell details, order and value of the cell")
	private List<CellBean> cellBeanList;
	
}
