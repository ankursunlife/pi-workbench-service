package com.aarete.pi.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.aarete.pi.bean.AssignClaimRequest;
import com.aarete.pi.bean.AssignClaimResponse;
import com.aarete.pi.bean.ClaimBean;
import com.aarete.pi.bean.ClaimLineBean;
import com.aarete.pi.bean.ClaimRequest;
import com.aarete.pi.bean.ClaimResponse;
import com.aarete.pi.bean.ClaimWorkflowRequest;
import com.aarete.pi.bean.ClaimlineAssignmentRequest;
import com.aarete.pi.bean.ClaimlineComment;
import com.aarete.pi.bean.ClaimlineCommentResponse;
import com.aarete.pi.bean.EngagementFilterRequest;
import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.bean.MyFilterRequestBean;
import com.aarete.pi.bean.ProviderResponseBean;
import com.aarete.pi.bean.SummaryResponse;
import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.ClaimlineCommentEntity;
import com.aarete.pi.bean.ClaimlineListDetailsResponse;
import com.aarete.pi.bean.ClaimlineListDetailsRequest;

public interface ClaimService {

    List<ClaimLineEntity> saveClaimLineList(List<ClaimLineEntity> claimLineEntities);

    SummaryResponse summaryBy(ClaimRequest claimRequest);

    ClaimResponse getClaimLineList(ClaimRequest claimRequest);

    List<ClaimLineEntity> getAllClaimLines();

    ClaimLineBean getClaimLineBean(Long claimLineId);
    
    ProviderResponseBean getProvidersDetails(EngagementFilterRequest engagementFilterRequest);

	void assignClaimLines(@Valid ClaimlineAssignmentRequest claimlineAssignmentRequest);

	String processClaims(@Valid ClaimWorkflowRequest claimWorkflowRequest);

	void export(@Valid ClaimRequest claimRequest);

	List<ClaimlineCommentEntity> addComment(@Valid ClaimlineComment claimlineComment);

	ClaimlineCommentResponse getComments(@Valid ClaimlineComment claimlineComment);

	List<IdNameBean> getMyCodeList(@Valid MyFilterRequestBean myFilterRequestBean);

	ClaimBean getClaimDetails(@Valid long claimNum);

	Optional<List<IdNameBean>> getBatchNumberList(@Valid MyFilterRequestBean myFilterRequestBean);

	ClaimBean getClaimsAllClaimlineList(@Valid long claimlineId, @Valid String engagementId);

	AssignClaimResponse processClaimLines(@Valid AssignClaimRequest assignCliamRequest, UserDetailsBean userDetails) throws Exception;

	ClaimlineListDetailsResponse getClaimLineListSummary(ClaimlineListDetailsRequest claimlineListDetailsRequest);
}
