package com.aarete.pi.dao;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.aarete.pi.bean.AssignClaimRequest;
import com.aarete.pi.bean.AssignClaimResponse;
import com.aarete.pi.bean.ClaimLineCount;
import com.aarete.pi.bean.ClaimRequest;
import com.aarete.pi.bean.ClaimlineAssignmentRequest;
import com.aarete.pi.bean.ClaimlineComment;
import com.aarete.pi.bean.EngagementFilterRequest;
import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.bean.MyFilterRequestBean;
import com.aarete.pi.bean.ProviderBean;
import com.aarete.pi.bean.SummaryDTO;
import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.entity.ClaimLineEntity;
import com.querydsl.core.Tuple;
import com.aarete.pi.bean.ClaimlineListDetailsResponse;
import com.aarete.pi.bean.ClaimlineListDetailsRequest;

public interface ClaimDAO {

    Optional<List<SummaryDTO>> groupBySummary(ClaimRequest claimRequest);

    List<ClaimLineEntity> claimLineList(ClaimRequest claimRequest);

    ClaimLineEntity getClaimLine(Long claimLineId);

	List<ProviderBean> getProviderList(EngagementFilterRequest engagementFilterRequest);

	void assignClaimLines(@Valid ClaimlineAssignmentRequest claimlineAssignmentRequest);

	List<ClaimlineComment> getComments(@Valid ClaimlineComment claimlineComment);

	List<Tuple> getCodes(MyFilterRequestBean myFilterRequestBean);

	Optional<ClaimLineCount> getClaimCount(ClaimRequest claimRequest);

	Optional<List<IdNameBean>> getBatchNumberList(MyFilterRequestBean myFilterRequestBean);

	AssignClaimResponse processClaimLines(@Valid AssignClaimRequest assignCliamRequest, UserDetailsBean userDetails) throws Exception;
	
	public List<Long> getClaimIds(Long claimNum);

	ClaimlineListDetailsResponse getClaimLineListSummary(ClaimlineListDetailsRequest claimlineListDetailsRequest);
}
