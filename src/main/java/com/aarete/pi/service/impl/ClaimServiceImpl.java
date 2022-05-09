package com.aarete.pi.service.impl;

import static com.aarete.pi.helper.ClaimServiceHelper.addExCodeDetails;
import static com.aarete.pi.helper.ClaimServiceHelper.addProviderMemberDiagnosisDetails;
import static com.aarete.pi.helper.ClaimServiceHelper.distinctByKey;
import static com.aarete.pi.helper.ClaimServiceHelper.sortResult;
import static java.lang.String.valueOf;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.querydsl.core.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.aarete.pi.bean.AssignClaimRequest;
import com.aarete.pi.bean.AssignClaimResponse;
import com.aarete.pi.bean.ClaimBean;
import com.aarete.pi.bean.ClaimLineBean;
import com.aarete.pi.bean.ClaimLineCount;
import com.aarete.pi.bean.ClaimProcessRequest;
import com.aarete.pi.bean.ClaimRequest;
import com.aarete.pi.bean.ClaimResponse;
import com.aarete.pi.bean.ClaimSummary;
import com.aarete.pi.bean.ClaimWorkflowRequest;
import com.aarete.pi.bean.ClaimlineAssignmentRequest;
import com.aarete.pi.bean.ClaimlineComment;
import com.aarete.pi.bean.ClaimlineCommentResponse;
import com.aarete.pi.bean.EngagementFilterRequest;
import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.bean.MyFilterRequestBean;
import com.aarete.pi.bean.ProviderBean;
import com.aarete.pi.bean.ProviderResponseBean;
import com.aarete.pi.bean.SummaryDTO;
import com.aarete.pi.bean.SummaryResponse;
import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.ClaimlineListDetailsResponse;
import com.aarete.pi.bean.ClaimlineListDetailsRequest;
import com.aarete.pi.dao.ClaimDAO;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.ClaimlineCommentEntity;
import com.aarete.pi.enums.ExCodeLevel;
import com.aarete.pi.helper.ExcelHelper;
import com.aarete.pi.helper.WorkflowHelper;
import com.aarete.pi.repository.ClaimLineCommentRepository;
import com.aarete.pi.repository.ClaimLineRepository;
import com.aarete.pi.service.ClaimService;
import com.google.common.collect.Lists;

@Service
public class ClaimServiceImpl implements ClaimService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${claim.level.excode.batch.count}")
	private int claimLevelExCodeBatchCount;

	@Value("${claimline.level.excode.batch.count}")
	private int claimLineLevelExCodeBatchCount;

	@Autowired
	private ClaimLineRepository claimLineRepository;

	@Autowired
	private ClaimDAO claimDAO;

	@Autowired
	private ClaimLineCommentRepository claimLineCommentRepository;

	@Autowired
	private WorkflowHelper workflowHelper;

	@Override
	public List<ClaimLineEntity> saveClaimLineList(List<ClaimLineEntity> ClaimLineEntities) {
		return claimLineRepository.saveAll(ClaimLineEntities);
	}

	@Override
	public SummaryResponse summaryBy(ClaimRequest claimRequest) {

		Optional<List<SummaryDTO>> optionalSummaryDTOs = claimDAO.groupBySummary(claimRequest);
		List<ClaimSummary> claimSummaries = new ArrayList<>();
		SummaryResponse summaryResponse = new SummaryResponse();
		ClaimLineCount claimLineCount = new ClaimLineCount();
		int openCount = 0; // TODO
		int pendCount = 0;
		int waitingCount = 0;

		if (optionalSummaryDTOs.isPresent()) {
			List<SummaryDTO> summaryDTOs = optionalSummaryDTOs.get();

			for (SummaryDTO summaryDTO : summaryDTOs) {

				ClaimSummary claimSummary = new ClaimSummary();
				claimSummary.setNameId(summaryDTO.getNameId());
				claimSummary.setNameValue(summaryDTO.getNameValue());
				claimSummary.setColumnOneValue(summaryDTO.getColumnOneValue());
				claimSummary.setColumnTwoValue(summaryDTO.getColumnTwoValue());
				claimSummaries.add(claimSummary);

				openCount += openCount;
				pendCount += pendCount;
				waitingCount += waitingCount;
			}

			claimLineCount.setMyQueueCount(openCount);
			claimLineCount.setPendCount(pendCount);
			claimLineCount.setWaitingCount(waitingCount);
			summaryResponse.setClaimLineCount(claimLineCount);

			double columnOneSum = claimSummaries.stream()
					.mapToDouble(summary -> Double.valueOf(summary.getColumnOneValue())).sum();
			double columnTwoSum = claimSummaries.stream()
					.mapToDouble(summary -> Double.valueOf(summary.getColumnTwoValue())).sum();
			DecimalFormat df = new DecimalFormat("#");
			ClaimSummary claimSummaryHeader = new ClaimSummary();
			claimSummaryHeader.setNameId(String.valueOf(0));
			claimSummaryHeader.setNameValue("All");
			claimSummaryHeader.setColumnOneValue(valueOf(df.format(columnOneSum)));
			claimSummaryHeader.setColumnTwoValue(valueOf(df.format(columnTwoSum)));

			summaryResponse.setClaimSummaryHeader(claimSummaryHeader);
			summaryResponse.setClaimSummaryList(sortResult(claimSummaries, claimRequest));
		}
		return summaryResponse;
	}

	@Override
	public ClaimResponse getClaimLineList(ClaimRequest claimRequest) {
		List<ClaimLineBean> claimLineBeanList = new ArrayList<>();
		List<ClaimLineEntity> claimLineEntities = claimDAO.claimLineList(claimRequest);
		for (ClaimLineEntity claimLineEntity : claimLineEntities) {
			ClaimLineBean claimLineBean = new ClaimLineBean();
			BeanUtils.copyProperties(claimLineEntity, claimLineBean);
			addExCodeDetails(claimLineEntity, claimLineBean);
			claimLineBeanList.add(claimLineBean);
		}
		ClaimResponse claimResponse = new ClaimResponse();
		claimResponse.setClaimLines(claimLineBeanList);
		Optional<ClaimLineCount> claimLineCountOptional = claimDAO.getClaimCount(claimRequest);
		if (claimLineCountOptional.isPresent()) {
			claimResponse.setClaimLineCount(claimLineCountOptional.get());
		}
		return claimResponse;
	}

	@Override
	public List<ClaimLineEntity> getAllClaimLines() {
		return claimLineRepository.findAll();// TODO this will not work as it will fetch all records of other users as
												// well
	}

	@Override
	public ClaimLineBean getClaimLineBean(Long claimLineId) {
		ClaimLineEntity claimLineEntity = claimDAO.getClaimLine(claimLineId);
		ClaimLineBean claimLineBean = null;
		if (Objects.nonNull(claimLineEntity)) {
			claimLineBean = new ClaimLineBean();
			BeanUtils.copyProperties(claimLineEntity, claimLineBean);
			addExCodeDetails(claimLineEntity, claimLineBean);
			addProviderMemberDiagnosisDetails(claimLineEntity, claimLineBean);
		}
		return claimLineBean;

	}

	@Override
	public ProviderResponseBean getProvidersDetails(EngagementFilterRequest engagementFilterRequest) {
		ProviderResponseBean providerResponseBean = null;
		List<ProviderBean> providerBeans = claimDAO.getProviderList(engagementFilterRequest);
		if (Objects.nonNull(providerBeans)) {
			providerResponseBean = new ProviderResponseBean();
			List<ProviderBean> uniqueProviderBeans = providerBeans.stream()
					.filter(distinctByKey(ProviderBean::getProviderFullName)).collect(Collectors.toList());

			List<IdNameBean> providerNames = uniqueProviderBeans.stream()
					.map(providerBean -> new IdNameBean(providerBean.getProviderNumber(),
							providerBean.getProviderFullName(), providerBean.getProviderFullName(), false))
					.collect(Collectors.toList());

			List<IdNameBean> providerIrs = uniqueProviderBeans.stream()
					.map(providerBean -> new IdNameBean(String.valueOf(providerBean.getProviderIrs()),
							String.valueOf(providerBean.getProviderIrs()),
							String.valueOf(providerBean.getProviderIrs()), false))
					.collect(Collectors.toList());

			List<IdNameBean> providerNpi = uniqueProviderBeans.stream()
					.map(providerBean -> new IdNameBean(String.valueOf(providerBean.getProviderNpi()),
							String.valueOf(providerBean.getProviderNpi()),
							String.valueOf(providerBean.getProviderNpi()), false))
					.collect(Collectors.toList());

			providerResponseBean.setProviderIRSList(providerIrs);
			providerResponseBean.setProviderNameList(providerNames);
			providerResponseBean.setProviderNPIList(providerNpi);
		}
		return providerResponseBean;
	}

	@Override
	public void assignClaimLines(@Valid ClaimlineAssignmentRequest claimlineAssignmentRequest) {
		claimDAO.assignClaimLines(claimlineAssignmentRequest);

	}

	@Override
	public String processClaims(@Valid ClaimWorkflowRequest claimWorkflowRequest) {
		int claimLevelExCodeSuccessCount = 0;
		int claimLevelExCodeFailedCount = 0;
		int claimLineLevelExCodeSuccessCount = 0;
		int claimLineLevelExCodeFailedCount = 0;

		List<ClaimProcessRequest> claimLevelExCodeClaimLines = new ArrayList<>();
		Map<String, List<ClaimProcessRequest>> claimLineMap = new HashMap<String, List<ClaimProcessRequest>>();
		for (ClaimProcessRequest claimProcessRequest : claimWorkflowRequest.getClaimProcessList()) {
			ExCodeLevel exCodeLevel = ExCodeLevel.getInstanceByName(claimProcessRequest.getExCodeLevel());
			String claimNum = String.valueOf(claimProcessRequest.getClaimNum());
			switch (exCodeLevel) {
			case CLAIM:
				claimLevelExCodeClaimLines.add(claimProcessRequest);
				break;
			case CLAIM_LINE:

				if (claimLineMap.containsKey(claimNum)) {
					claimLineMap.get(claimNum).add(claimProcessRequest);
				} else {
					List<ClaimProcessRequest> claimLineLevelExCodeClaimLines = new ArrayList<>();
					claimLineLevelExCodeClaimLines.add(claimProcessRequest);
					claimLineMap.put(claimNum, claimLineLevelExCodeClaimLines);
				}
			default:
				break;
			}
		}
		List<CompletableFuture<Map<Integer, List<ClaimProcessRequest>>>> claimCompletableFutures = new ArrayList<>();
		List<CompletableFuture<Map<Integer, List<ClaimProcessRequest>>>> claimLineCompletableFutures = new ArrayList<>();

		// TODO remove the duplicated code make generic, probably use another class or
		// design pattern
		for (List<ClaimProcessRequest> partitionedClaims : Lists.partition(claimLevelExCodeClaimLines,
				claimLevelExCodeBatchCount)) {
			ClaimWorkflowRequest claimWorkflowRequestClaimLevel = new ClaimWorkflowRequest();
			claimWorkflowRequestClaimLevel.setClaimProcessList(partitionedClaims);
			CompletableFuture<Map<Integer, List<ClaimProcessRequest>>> completableFuture = workflowHelper
					.sendClaimLevelAccept(claimWorkflowRequestClaimLevel);
			claimCompletableFutures.add(completableFuture);
		}
		for (String claimNum : claimLineMap.keySet()) {

			ClaimWorkflowRequest claimWorkflowRequestClaimLineLevel = new ClaimWorkflowRequest();
			claimWorkflowRequestClaimLineLevel.setClaimProcessList(claimLineMap.get(claimNum));
			CompletableFuture<Map<Integer, List<ClaimProcessRequest>>> completableFuture = workflowHelper
					.sendClaimLineLevelAccept(claimWorkflowRequestClaimLineLevel);
			claimLineCompletableFutures.add(completableFuture);
		}

		// Wait until they are all done
		CompletableFuture.allOf(claimCompletableFutures.toArray(CompletableFuture[]::new)).join();

		// Wait until they are all done
		CompletableFuture.allOf(claimLineCompletableFutures.toArray(CompletableFuture[]::new)).join();

		for (CompletableFuture<Map<Integer, List<ClaimProcessRequest>>> completableFuture : claimCompletableFutures) {
			Map<Integer, List<ClaimProcessRequest>> httpStatusListMap = null;
			try {
				httpStatusListMap = completableFuture.get();
			} catch (InterruptedException e) {
				// TODO handle this exception
			} catch (ExecutionException e) {
				// TODO handle this exception
			}
			if (httpStatusListMap.containsKey(HttpStatus.OK.value())) {
				claimLevelExCodeSuccessCount += claimLevelExCodeBatchCount;
			} else {
				claimLevelExCodeFailedCount += claimLevelExCodeBatchCount;
			}
		}

		for (CompletableFuture<Map<Integer, List<ClaimProcessRequest>>> completableFuture : claimLineCompletableFutures) {
			Map<Integer, List<ClaimProcessRequest>> httpStatusListMap = null;
			try {
				httpStatusListMap = completableFuture.get();
			} catch (InterruptedException e) {
				// TODO handle this exception
			} catch (ExecutionException e) {
				// TODO handle this exception
			}
			if (httpStatusListMap.containsKey(HttpStatus.OK.value())) {
				claimLineLevelExCodeSuccessCount += claimLineLevelExCodeBatchCount;
			} else {
				claimLineLevelExCodeFailedCount += claimLineLevelExCodeBatchCount;
			}
		}

		// TODO Send above maps to respective lambda's via API Gateway in batches of 10
		// (Configurable for both Claim and Claimline level lambdas from properties
		// file)
		// pick URLs from properties files, there will be separate URL for claim and
		// claimline level and also for different workflows (ACCEPT, REJECT etc)
		// Make all lambda calls Async. Do check a status of lambda before sending
		// status back to the user.
		// Return status something like 5 claimline and 2 claims sent for Approval.
		// Tips create some workflow helper class
		return String.format(
				"%s claims and %s claimlines sent for Approval, out off %s claims and %s claimlines failed.",
				claimLevelExCodeSuccessCount, claimLineLevelExCodeSuccessCount, claimLevelExCodeFailedCount,
				claimLineLevelExCodeFailedCount);
	}

	@Override
	public void export(@Valid ClaimRequest claimRequest) {
		List<ClaimLineEntity> claimLineEntities;
		List<ClaimLineBean> claimLineBeanList = null;
		try {
			claimLineEntities = claimDAO.claimLineList(claimRequest);
			claimLineBeanList = new ArrayList<>();
			for (ClaimLineEntity claimLineEntity : claimLineEntities) {
				ClaimLineBean claimLineBean = new ClaimLineBean();
				BeanUtils.copyProperties(claimLineEntity, claimLineBean);
				claimLineBeanList.add(claimLineBean);
			}
			ExcelHelper.exportExcel(claimLineBeanList, claimRequest.getExportRequest());
		} catch (Exception e) {
			log.error("Exception in creating export", e);
			// TODO handle this exception
		}
	}

	@Override
	public List<ClaimlineCommentEntity> addComment(@Valid ClaimlineComment claimlineComment) {
		List<ClaimlineCommentEntity> claimLineCommentEntities = new ArrayList<>();
		for (long claimLineId : claimlineComment.getClaimLineIdList()) {
			ClaimlineCommentEntity claimlineCommentEntity = new ClaimlineCommentEntity();
			claimlineCommentEntity.setComment(claimlineComment.getComment());
			claimlineCommentEntity.setCommentTime(Timestamp.from(Instant.now()));
			claimlineCommentEntity.setUserId(claimlineComment.getUserId());
			claimlineCommentEntity.setCommentedByType(claimlineComment.getEngagementRole()); // AArete or Client person
			claimlineCommentEntity.setClaimLineId(claimLineId);
			claimLineCommentEntities.add(claimlineCommentEntity);
		}
		return claimLineCommentRepository.saveAll(claimLineCommentEntities);
	}

	@Override
	public ClaimlineCommentResponse getComments(@Valid ClaimlineComment claimlineComment) {
		ClaimlineCommentResponse claimlineCommentResponse = new ClaimlineCommentResponse();
		claimlineCommentResponse.setClaimlineCommentList(claimDAO.getComments(claimlineComment));
		return claimlineCommentResponse;
	}

	@Override
	public List<IdNameBean> getMyCodeList(@Valid MyFilterRequestBean myFilterRequestBean) {
		List<Tuple> allCodes = claimDAO.getCodes(myFilterRequestBean);
		return allCodes.stream().map(code -> {
			Object[] objects = code.toArray();
			return new IdNameBean((String) objects[0], (String) objects[0], (String) objects[1]);
		}).collect(Collectors.toList());
	}

	@Override
	public ClaimBean getClaimDetails(@Valid long claimNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<List<IdNameBean>> getBatchNumberList(@Valid MyFilterRequestBean myFilterRequestBean) {
		return claimDAO.getBatchNumberList(myFilterRequestBean);
	}

	@Override
	public ClaimBean getClaimsAllClaimlineList(@Valid long claimlineId, @Valid String engagementId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public AssignClaimResponse processClaimLines(@Valid AssignClaimRequest assignCliamRequest,
			UserDetailsBean userDetails) throws Exception {
		return claimDAO.processClaimLines(assignCliamRequest, userDetails);
	}

	@Override
	public ClaimlineListDetailsResponse getClaimLineListSummary(ClaimlineListDetailsRequest claimlineListDetailsRequest) {
		return claimDAO.getClaimLineListSummary(claimlineListDetailsRequest);
	}

}
