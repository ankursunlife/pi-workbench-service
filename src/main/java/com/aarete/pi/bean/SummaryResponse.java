package com.aarete.pi.bean;

import java.util.List;

import lombok.Data;

@Data
public class SummaryResponse {

	private SummaryByRequest summaryByRequest; // TODO
	
	private List<ClaimSummary> claimSummaryList;

	private ClaimLineCount claimLineCount;
	
	private ClaimSummary claimSummaryHeader;
}
