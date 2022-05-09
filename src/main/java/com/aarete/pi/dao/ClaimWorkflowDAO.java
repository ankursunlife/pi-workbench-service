package com.aarete.pi.dao;

import com.aarete.pi.bean.ClaimStatusRequest;

public interface ClaimWorkflowDAO {

    long updateClaimStatusCode(ClaimStatusRequest claimStatusRequest);
}
