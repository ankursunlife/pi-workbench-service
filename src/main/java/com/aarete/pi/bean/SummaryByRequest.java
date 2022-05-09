package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SummaryByRequest {

	@ApiModelProperty(notes = "Get from SummaryBy drop down", example = "EDIT, EX_CODE, PROVIDER_NAME, PROVIDER_NPI, PROVIDER_TIN, CPT_CODE, REV_CODE, BILL_TYPE, POS, BATCH, DATE_OF_SERVICE, CLAIM_PAID_DATE, CLAIM_RECEIVE_DATE")
	private String summaryBy;

	@ApiModelProperty(notes = "Request of claimline list. If Provider is selected in Summary By dropdown, send Provider ID", example = "If Provider is selected in Summary By dropdown, send Provider ID")
	private String summaryByValue; 
	
	@ApiModelProperty(notes = "", example = "CLAIMS, CLAIM_LINES, UNITS, PAID, OPPORTUNITY, BILLED, NAME")
	private String columnOneName;
	
	@ApiModelProperty(notes = "", example = "CLAIMS, CLAIM_LINES, UNITS, PAID, OPPORTUNITY, BILLED, NAME")
	private String columnTwoName;

	@ApiModelProperty(notes = "ALL, TOP 10, TOP 20, can take any number", example = "10,20,30,9999999")
	private int recordCount;

	@ApiModelProperty(notes = "sort by column name", example = "CLAIMS, CLAIM_LINES, UNITS, PAID, OPPORTUNITY, BILLED, NAME")
	private String sortBy;

	@ApiModelProperty(notes = "sort by column value", example = "ASC, DESC")
	private String sortType = "DESC";
	
}
