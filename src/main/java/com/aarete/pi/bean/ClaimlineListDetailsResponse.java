package com.aarete.pi.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "Response bean to get the list of claimline details of the claim/line level excode ")
public class ClaimlineListDetailsResponse {
    @ApiModelProperty(notes = "List of claimline details")
    private List<ClaimLineBean> claimLines;
}
