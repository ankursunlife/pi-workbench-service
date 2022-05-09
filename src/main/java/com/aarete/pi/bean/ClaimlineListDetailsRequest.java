package com.aarete.pi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "Request bean to send request to get the summary of the provided claimline list")
public class ClaimlineListDetailsRequest {

    @ApiModelProperty(notes = "Put claimline ids which has claim level ex codes")
    private List<Long> claimLevelClaimLineIdList = new ArrayList<>();

    @ApiModelProperty(notes = "Put claimline ids which has claimline level ex codes")
    private List<Long> claimlineLevelClaimLineIdList = new ArrayList<>();

    @NotEmpty(message = "Engagement Role is mandatory.")
    @ApiModelProperty(notes = "Role based on selected engagement", example = "AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER")
    private String engagementRole;

    @NotEmpty(message = "Engagement ID is mandatory.")
    @ApiModelProperty(notes = "Engagement Id is mandatory")
    private String engagementId;
}
