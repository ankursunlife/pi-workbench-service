package com.aarete.pi.service;

import com.aarete.pi.bean.ClaimDetailsRequest;
import com.aarete.pi.bean.ClaimDetailsResponse;
import com.aarete.pi.bean.ClaimsListRequest;
import com.aarete.pi.bean.ClaimsListResponse;

public interface DataLakeService {
	
	ClaimDetailsResponse getClaimDetails(ClaimDetailsRequest claimDetailsRequest);
	
	ClaimsListResponse getClaimsList(ClaimsListRequest claimsListRequest);

}
