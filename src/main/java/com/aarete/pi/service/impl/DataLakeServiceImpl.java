package com.aarete.pi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aarete.pi.bean.ClaimDetailsRequest;
import com.aarete.pi.bean.ClaimDetailsResponse;
import com.aarete.pi.bean.ClaimsListRequest;
import com.aarete.pi.bean.ClaimsListResponse;
import com.aarete.pi.helper.AWSAthenaServiceHelper;
import com.aarete.pi.service.DataLakeService;

@Service
public class DataLakeServiceImpl implements DataLakeService {
	@Autowired
	AWSAthenaServiceHelper awsAthenaServiceHelper;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	@Override
	public ClaimDetailsResponse getClaimDetails(ClaimDetailsRequest claimDetailsRequest) {
		// Get DB name for engagementId (Dynamo DB config?)
		ClaimDetailsResponse claimDetailsResponse = null;
		try {
			claimDetailsResponse = awsAthenaServiceHelper.getClaimDetails(claimDetailsRequest);
		} catch (InterruptedException e) {
			log.error("Exception executing Athena query: {}", e);
		}
		return claimDetailsResponse;
	}

	@Override
	public ClaimsListResponse getClaimsList(ClaimsListRequest claimsListRequest) {
		ClaimsListResponse claimsListResponse = null;
		try {
			claimsListResponse = awsAthenaServiceHelper.getClaimsList(claimsListRequest);
		} catch (InterruptedException e) {
			log.error("Exception executing Athena query: {}", e);
		}
		return claimsListResponse;
	}
}
