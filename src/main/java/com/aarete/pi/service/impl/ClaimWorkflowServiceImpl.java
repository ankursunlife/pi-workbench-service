package com.aarete.pi.service.impl;

import com.aarete.pi.bean.ClaimStatusRequest;
import com.aarete.pi.bean.ClaimWorkflowStatusRequest;
import com.aarete.pi.dao.ClaimWorkflowDAO;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.service.ClaimWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClaimWorkflowServiceImpl implements ClaimWorkflowService {

    /*@Autowired
    private ClaimLineRepository claimLineRepository;*/

    @Autowired
    private ClaimWorkflowDAO claimWorkflowDAO;

    @Override
    @Transactional
    public void updateClaimStatus(ClaimWorkflowStatusRequest claimWorkflowStatusRequest) throws RecordNotFound {
        for (ClaimStatusRequest claimStatusRequest : claimWorkflowStatusRequest.getClaimStatusList()) {
            /*Optional<ClaimLineEntity> byId = claimLineRepository.findById(claimStatusRequest.getClaimLineId());
            if(byId.isPresent()) {
                ClaimLineEntity claimLineEntity = byId.get();
                claimLineEntity.setClaimStatusCode(claimStatusRequest.getClaimStatusCode());
                claimLineRepository.save(claimLineEntity);
            }*/
            long count = claimWorkflowDAO.updateClaimStatusCode(claimStatusRequest);
            if( count <= 0) {
                throw new RecordNotFound("No claim to update claim status code");
            }
        }
    }
}
