package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClaimDownloadRequest {
	
	@ApiModelProperty
	private ClaimRequest claimRequest;
	
	@ApiModelProperty
	private ExcelSheetBean excelSheetBean;

}
