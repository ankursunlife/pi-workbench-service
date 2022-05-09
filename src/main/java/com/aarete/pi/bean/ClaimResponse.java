package com.aarete.pi.bean;

import lombok.Data;

import java.util.List;

import io.swagger.annotations.ApiModel;

@Data
@ApiModel(description = "This bean will provide filtered claimline list for the user.")
public class ClaimResponse {

	private List<ClaimLineBean> claimLines;

	private ClaimLineCount claimLineCount;
}
