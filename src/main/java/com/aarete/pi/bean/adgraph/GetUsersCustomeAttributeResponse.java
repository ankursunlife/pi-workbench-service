package com.aarete.pi.bean.adgraph;

import java.util.List;

import lombok.Data;

@Data
public class GetUsersCustomeAttributeResponse {
	
	private String clientName;
	private String engagementId;
	private List<String> exCodes;
}
