package com.aarete.pi.bean;

import java.util.List;

import lombok.Data;

@Data
public class ClaimDetailsResponse {

	private List<ClaimLineInfo> claimLines;
	
	private String queryExecutionId;
	
	private String nextToken;
}
