package com.aarete.pi.service;

import com.aarete.pi.bean.ClaimWorkflowStatusRequest;
import com.aarete.pi.exception.RecordNotFound;

public interface ClaimWorkflowService {

    void updateClaimStatus(ClaimWorkflowStatusRequest claimWorkflowStatusRequest) throws RecordNotFound;

}
