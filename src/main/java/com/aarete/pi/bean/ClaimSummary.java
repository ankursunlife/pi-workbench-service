package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClaimSummary {

	@ApiModelProperty(notes = "Id of the selected summary by drop down", example = "providerId, billTypeId")
	private String nameId;

	@ApiModelProperty(notes = "Value of the selected summary by drop down", example = "providerName, billType")
	private String nameValue;

	@ApiModelProperty(notes = "Value of the second column in summary by section", example = "claims, claim lines, billed")
	private String columnOneValue;

	@ApiModelProperty(notes = "Value of the third column in summary by section", example = "claims, claim lines, billed")
	private String columnTwoValue;

}
