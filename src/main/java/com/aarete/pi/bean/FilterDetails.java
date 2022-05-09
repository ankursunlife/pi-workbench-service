package com.aarete.pi.bean;

import java.sql.Date;
import java.util.List;

import javax.validation.constraints.Size;

import com.aarete.pi.enums.ClaimStatus;
import com.aarete.pi.util.EnumValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Bean to send filter details. This bean has non mandatory fields. " +
		"If field is sent filter will be applied, else all claimlines will be populated")
public class FilterDetails {

	private List<String> stateIdList;

	private List<String> lobIdList;

	private List<String> providerIdList;

	private List<Long> providerNPIList;

	private List<String> providerSpecialityList;

	private List<CodeType> codeTypes;

	private List<String> claimFormTypeIdList;

	@Size(min = 0, message = "Claim Billed Amount Range - Minimum ")
	@ApiModelProperty(notes = "Claim Billed Amount Range - Minimum", example = "0")
	private long billedAmountStart;

	@Size(min = 0, message = "Claim Billed Amount Range - Maximum ")
	@ApiModelProperty(notes = "Claim Billed Amount Range - Maximum", example = "0")
	private long billedAmountEnd;

	private long allowedAmountStart;

	private long allowedAmountEnd;

	private long paidAmountStart;

	private long paidAmountEnd;

	private long opportunityAmountStart;

	private long opportunityAmountEnd;

	private Date receivedDateStart;

	private Date receivedDateEnd;

	private Date paidDateStart;

	private Date paidDateEnd;

	private Date serviceDateStart;

	private Date serviceDateEnd;

	private List<Long> auditList;

	private boolean tagged;

	@ApiModelProperty(notes = "Send this when you want for specific types of claims",example = "GROUP_QUEUE, MY_QUEUE, PEND, WAITING")
	private String claimStatus;

	private List<String> billTypeCodes;

	private List<Long> providerIRSList;

	private List<Integer> pos;

	private long modelScoreStart;

	private long modelScoreEnd;

	private List<String> memberStates;

	private List<String> subProducts;

	private float prioritizationScoreStart;
	
	private float prioritizationScoreEnd;
	
	private Date batchStartDate;
	
	private Date batchEndDate;
	
	private List<Long> batchNumberList;

	private List<String> pillarIds;

	private List<String> edits;

	private float confidenceScore;

	private List<String> exCodes;

	private Date claimStartDateStart;

	private Date claimStartDateEnd;

	private Date claimEndDateStart;

	private Date claimEndDateEnd;

	private String claimNum;

	private List<String> providerStateIdList;

	private long adjustmentAmountStart;

	private long adjustmentAmountEnd;

}
