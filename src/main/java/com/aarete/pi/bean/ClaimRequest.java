package com.aarete.pi.bean;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClaimRequest {

	@ApiModelProperty(notes = "Send this if values are selected in Filter section, for request of Summary list as well as Claimline list.")
	private FilterDetails filters;
	
	@ApiModelProperty(notes = "Send this for request of Summary list as well as Claimline list, send values selected from landing page.")
	private EngagementFilterRequest engagementFilters;
	
	@ApiModelProperty(notes = "Send this for request of Summary list only.")
	private SummaryByRequest summaryBy;
	
	@ApiModelProperty(notes = "Send this for request for export related functionality. Make sure to send FilterDetails and EngagementFilterRequest details as well.")
	private ExportRequest exportRequest;
	
	@ApiModelProperty(notes = "Populate this when sending SummaryBy request or Claimline list.")
	private SortingAndPaginationBean sortingAndPagination;

}
