package com.aarete.pi.bean;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExportRequest {
	
	@ApiModelProperty(notes = "Export type, PDF is not yet supported", example = "EXCEL, CSV")
	private String exportType;
	
	@ApiModelProperty(notes = "File Name without extension", example = "Provider report")
	private String fileName;
	
	@ApiModelProperty(notes = "Send 1 ExcelSheetBean for each sheet that needs to be created in excel. Send 1 bean for CSV.")
	List<ExcelSheetBean> excelSheetBeans;
	
	@ApiModelProperty(notes = "Send 1 ExcelSheetBean for each sheet that needs to be created in excel. Send 1 bean for CSV.")
	private String playlistId;
}
