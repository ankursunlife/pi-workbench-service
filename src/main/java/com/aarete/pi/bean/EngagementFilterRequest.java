package com.aarete.pi.bean;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.aarete.pi.enums.ClaimStatus;
import com.aarete.pi.enums.Role;
import com.aarete.pi.util.EnumValidation;
import org.springframework.stereotype.Component;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Component
public class EngagementFilterRequest {

	@ApiModelProperty(notes = "Engagement Id is mandatory")
	@NotEmpty(message = "Engagement ID is mandatory.")
	private String engagementId;

	private List<String> piPillarList;
	
	private List<String> stateList;
	
	private List<String> lobList;
	
	@ApiModelProperty(notes = "Role based on selected engagement", example = "AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER")
	@NotEmpty(message = "Engagement Role is mandatory.")
	private String engagementRole;

	@ApiModelProperty(notes = "For internal purpose only, do not send any value here", example = "DO NOT SET ANYTHING")
	private String loggedInUserEmailId;

	@ApiModelProperty(notes = "For internal purpose only, do not send any value here", example = "DO NOT SET ANYTHING")
	private List<String> groupNames;

}
