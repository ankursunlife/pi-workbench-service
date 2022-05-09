package com.aarete.pi.bean;

import java.util.List;

import lombok.Data;

@Data
public class ClaimsListResponse {
	
	List<ClaimBean> claims;//TODO make it private
	
	private String queryExecutionId;
	
	private String nextToken;
}
