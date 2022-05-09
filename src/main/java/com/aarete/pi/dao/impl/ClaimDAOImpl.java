package com.aarete.pi.dao.impl;

import static com.aarete.pi.constant.WorkbenchConstants.CLAIM_LINE_MSG_FORMAT;
import static com.aarete.pi.enums.SummaryBy.getInstanceByName;
import static com.aarete.pi.helper.ClaimDAOHelper.applyAllFilters;
import static com.aarete.pi.helper.ClaimDAOHelper.applyClaimStatusFilter;
import static com.aarete.pi.helper.ClaimDAOHelper.applyEngagementFilters;
import static com.aarete.pi.helper.ClaimDAOHelper.applySummaryByFilter;
import static com.aarete.pi.helper.ClaimDAOHelper.groupByClaimStatus;
import static com.aarete.pi.helper.ClaimDAOHelper.groupBySummaryQuery;
import static com.aarete.pi.helper.ClaimDAOHelper.isAlreadyAssigned;
import static com.aarete.pi.helper.ClaimDAOHelper.setCount;
import static com.aarete.pi.helper.ClaimDAOHelper.transformToBatchIdNameBean;
import static com.aarete.pi.helper.ClaimDAOHelper.transformToComments;
import static com.aarete.pi.helper.ClaimDAOHelper.transformToProviderBean;
import static com.aarete.pi.helper.ClaimDAOHelper.addClaimLineBeanToList;
import static com.aarete.pi.helper.ClaimServiceHelper.sortClaimLineListResult;
import static org.springframework.util.StringUtils.hasText;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aarete.pi.bean.AssignClaimRequest;
import com.aarete.pi.bean.AssignClaimResponse;
import com.aarete.pi.bean.ClaimLineCount;
import com.aarete.pi.bean.ClaimProcessRequest;
import com.aarete.pi.bean.ClaimRequest;
import com.aarete.pi.bean.ClaimlineAssignmentRequest;
import com.aarete.pi.bean.ClaimlineComment;
import com.aarete.pi.bean.EngagementFilterRequest;
import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.bean.MyFilterRequestBean;
import com.aarete.pi.bean.ProviderBean;
import com.aarete.pi.bean.SummaryDTO;
import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.ClaimLineBean;
import com.aarete.pi.bean.ClaimlineListDetailsResponse;
import com.aarete.pi.bean.ClaimlineListDetailsRequest;
import com.aarete.pi.dao.ClaimDAO;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.ClaimlineCommentEntity;
import com.aarete.pi.entity.QClaimLineEntity;
import com.aarete.pi.entity.QClaimlineCommentEntity;
import com.aarete.pi.entity.QUserEntity;
import com.aarete.pi.enums.ClaimAction;
import com.aarete.pi.enums.ClaimStatus;
import com.aarete.pi.enums.ExCodeLevel;
import com.aarete.pi.enums.Role;
import com.aarete.pi.enums.Codes;
import com.aarete.pi.enums.SummaryBy;
import com.aarete.pi.helper.ClaimDAOHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

@Component
public class ClaimDAOImpl implements ClaimDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClaimDAOImpl.class);
	
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<List<SummaryDTO>> groupBySummary(ClaimRequest claimRequest) {
        BooleanBuilder builder = new BooleanBuilder();
        final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;

        applyAllFilters(claimRequest, builder, claimLine);

        SummaryBy summaryBy = getInstanceByName(claimRequest.getSummaryBy().getSummaryBy());

        Optional<List<Tuple>> groupBySummaryOptional = groupBySummaryQuery(builder.getValue(), query, claimLine, summaryBy, claimRequest);
        return groupBySummaryOptional.flatMap(ClaimDAOHelper::transformToDTO);
    }

    @Override
    public List<ClaimLineEntity> claimLineList(ClaimRequest claimRequest) {
        BooleanBuilder builder = new BooleanBuilder();
        final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;

        applyAllFilters(claimRequest, builder, claimLine);
        applySummaryByFilter(claimRequest, builder, claimLine);
        applyClaimStatusFilter(claimRequest, builder, claimLine);

        OrderSpecifier<?> sorting = sortClaimLineListResult(claimLine, claimRequest);

        return query.from(claimLine).where(builder.getValue()).orderBy(sorting).limit(claimRequest.getSortingAndPagination().getCount())
                .offset(claimRequest.getSortingAndPagination().getPageNumber() * claimRequest.getSortingAndPagination().getCount()).fetch();
    }

    @Override
    public ClaimLineEntity getClaimLine(Long claimLineId) {
        BooleanBuilder builder = new BooleanBuilder();
        final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;

        if (claimLineId > 0) {
            builder.and(claimLine.claimLineId.eq(claimLineId));
        }
        return query.from(claimLine).where(builder.getValue()).fetchOne();
    }

	@Override
	public List<ProviderBean> getProviderList(EngagementFilterRequest engagementFilterRequest) {

        BooleanBuilder builder = new BooleanBuilder();
        final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;

        applyEngagementFilters(engagementFilterRequest, builder, claimLine);
        List<Tuple> fetch = query.select(claimLine.billingProviderNumber, claimLine.billingProviderFullName, claimLine.billingProviderNpi, claimLine.billingProviderIrs)
                .distinct().from(claimLine).where(builder.getValue()).fetch();
        return transformToProviderBean(fetch);
	}

	@Override
	public void assignClaimLines(@Valid ClaimlineAssignmentRequest claimlineAssignmentRequest) {
		// TODO iterate on claimline level and assign 
		
	}

	@Override
	public List<ClaimlineComment> getComments(@Valid ClaimlineComment claimlineComment) {
        BooleanBuilder builder = new BooleanBuilder();
        final JPAQuery<ClaimlineCommentEntity> query = new JPAQuery<>(entityManager);
        final QClaimlineCommentEntity claimLineComment = QClaimlineCommentEntity.claimlineCommentEntity;
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;
        final QUserEntity userDetails = QUserEntity.userEntity;

        if (!CollectionUtils.isEmpty(claimlineComment.getClaimLineIdList())) {
            builder.and(claimLineComment.claimLineId.in(claimlineComment.getClaimLineIdList()));
        }

        // AARETE_USER, AARETE_MANAGER can see all the comments, CLIENT_USER, CLIENT_MANAGER can see each other comments
        if (hasText(claimlineComment.getEngagementRole()) && (Role.CLIENT_USER.toString().equals(claimlineComment.getEngagementRole()) 
        		|| Role.CLIENT_MANAGER.toString().equals(claimlineComment.getEngagementRole()))) {
            builder.and(claimLineComment.commentedByType.in(Role.CLIENT_USER.name(), Role.CLIENT_MANAGER.name()));
        }

        List<Tuple> fetch = query.select(claimLineComment, claimLine.claimNum, claimLine.claimLineNum, userDetails.userName).from(claimLineComment)
        		.innerJoin(claimLine).on(claimLineComment.claimLineId.eq(claimLine.claimLineId))
        		.leftJoin(userDetails).on(claimLineComment.userId.eq(userDetails.userId))
        		.where(builder.getValue()).fetch();
        return transformToComments(fetch);
	}

    @Override
    public List<Tuple> getCodes(MyFilterRequestBean myFilterRequestBean) {
        Codes instanceByName = Codes.getInstanceByName(myFilterRequestBean.getFilterName());
        final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
        BooleanBuilder builder = new BooleanBuilder();
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;
        applyEngagementFilters(myFilterRequestBean.getEngagementFilters(), builder, claimLine);
        switch (instanceByName) {
            case CPT:
                return query.distinct().from(claimLine).select(claimLine.cptCode,claimLine.cptDesc).where(builder.getValue()).fetch();
            case DX:
                return query.distinct().from(claimLine).select(claimLine.diagnosisCode1,claimLine.diagnosis1Desc).where(builder.getValue()).fetch();
            case REV:
                return query.distinct().from(claimLine).select(claimLine.revenueCode,claimLine.revenueDesc).where(builder.getValue()).fetch();
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public Optional<ClaimLineCount> getClaimCount(ClaimRequest claimRequest) {
        BooleanBuilder builder = new BooleanBuilder();
         JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;

        // TODO claim count is not populating for some request.
        claimRequest.getFilters().setClaimStatus(ClaimStatus.GROUP_QUEUE.name());
        applyAllFilters(claimRequest, builder, claimLine);
        Optional<List<Tuple>> groupByClaimStatusOptional = groupByClaimStatus(builder.getValue(), query, claimLine, claimRequest);

        builder = new BooleanBuilder();
        query = new JPAQuery<>(entityManager);
        claimRequest.getFilters().setClaimStatus(ClaimStatus.MY_QUEUE.name());
        applyAllFilters(claimRequest, builder, claimLine);
        Optional<List<Tuple>> groupByClaimStatusOptional1 = groupByClaimStatus(builder.getValue(), query, claimLine, claimRequest);
        if (groupByClaimStatusOptional1.isPresent() || groupByClaimStatusOptional.isPresent()) {
            List<Tuple> tuples = groupByClaimStatusOptional.get();
            tuples.addAll(groupByClaimStatusOptional1.get());
            ClaimLineCount claimLineCount = new ClaimLineCount();
            tuples.forEach(tuple -> setCount(tuple, claimLineCount));
            return Optional.of(claimLineCount);
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<IdNameBean>> getBatchNumberList(MyFilterRequestBean myFilterRequestBean) {
        BooleanBuilder builder = new BooleanBuilder();
        final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
        final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;

        applyEngagementFilters(myFilterRequestBean.getEngagementFilters(), builder, claimLine);
        List<Long> fetch = query.select(claimLine.batchId).distinct().from(claimLine).where(builder.getValue()).fetch();
        return transformToBatchIdNameBean(fetch);
    }

	@Override
	public AssignClaimResponse processClaimLines(AssignClaimRequest assignClaimRequest, UserDetailsBean userDetails) throws Exception{
        Map<Long, ClaimProcessRequest> claimsMap = new HashMap<>();
        Map<Long, ClaimProcessRequest> claimLineIdsMap = new HashMap<>();
        AssignClaimResponse response = new AssignClaimResponse();
        List<String> successList = new ArrayList<>();
        List<String> failedList = new ArrayList<>();
        
        final JPAQueryFactory jpaQuery = new JPAQueryFactory(entityManager);
        final QClaimLineEntity qClaimLineEntity = QClaimLineEntity.claimLineEntity;
        
		for(ClaimProcessRequest processRequest : assignClaimRequest.getClaimProcessList()) {
			if(ClaimAction.ASSIGN.name().equalsIgnoreCase(processRequest.getActionTaken()) || ClaimAction.PEND.name().equalsIgnoreCase(processRequest.getActionTaken())) {
				ExCodeLevel exCodeLevel = ExCodeLevel.getInstanceByName(processRequest.getExCodeLevel());
				if(ExCodeLevel.CLAIM.name().equals(exCodeLevel.name())) {
					claimsMap.put(processRequest.getClaimNum(), processRequest);
				}else if(ExCodeLevel.CLAIM_LINE.name().equals(exCodeLevel.name())){
					claimLineIdsMap.put(processRequest.getClaimLineId(), processRequest);
				}
			}
		}
		// update for Claim level ExCode
		if(!CollectionUtils.isEmpty(claimsMap)) {
			for(Map.Entry<Long, ClaimProcessRequest> claimDtls : claimsMap.entrySet()) {
				try {
					//Updating claimNumber
					updateClaimLines(qClaimLineEntity, jpaQuery, userDetails, claimDtls.getKey(), null,claimDtls.getValue(), successList, failedList);
				}catch (SQLException ex) {
					throw ex;
				}
			}
		}
		
		// update for CLiamLines level excode
		if(!CollectionUtils.isEmpty(claimLineIdsMap)) {
			for(Map.Entry<Long, ClaimProcessRequest> claimLineDtls : claimLineIdsMap.entrySet()) {
				try {
					//Updating claimLineNumber
					ClaimProcessRequest processRequest = claimLineDtls.getValue();
					updateClaimLines(qClaimLineEntity, jpaQuery, userDetails, processRequest.getClaimNum(), claimLineDtls.getKey(), processRequest, successList, failedList);
				}catch (SQLException e) {
					throw e;
				}
			}
		} 
		response.setAssignClaimSuccessList(successList);
		response.setAssignClaimFailedList(failedList);
		return response;
	}
	
	/**
	 * @param qClaimLineEntity
	 * @param jpaQuery
	 * @param userDetails
	 * @param claimNum
	 * @param claimLinesId
	 * @param processRequest
	 * @param successList
	 * @param failedList
	 * @return
	 * @throws SQLException
	 */
	private static long updateClaimLines(QClaimLineEntity qClaimLineEntity, JPAQueryFactory jpaQuery, UserDetailsBean userDetails, 
			Long claimNum, Long claimLinesId, ClaimProcessRequest processRequest, List<String> successList, List<String> failedList) throws SQLException {
		long count;
		List<Long> claimLineIds = new ArrayList<>();
		List<Integer> claimLineNums = new ArrayList<>();
		ExCodeLevel exCodeLevel = ExCodeLevel.getInstanceByName(processRequest.getExCodeLevel());
		List<Tuple> claimsRecords = jpaQuery.from(qClaimLineEntity).select(qClaimLineEntity.claimLineId, qClaimLineEntity.claimLineNum, 
				qClaimLineEntity.approverLevelOne, qClaimLineEntity.approverLevelTwo, qClaimLineEntity.approverLevelThree, qClaimLineEntity.approverLevelFour)
				.where(ExCodeLevel.CLAIM.name().equals(exCodeLevel.name()) ? qClaimLineEntity.claimNum.eq(claimNum) : qClaimLineEntity.claimLineId.eq(claimLinesId))
				.fetch();

		if(ClaimAction.ASSIGN.name().equalsIgnoreCase(processRequest.getActionTaken())){
		    for (Tuple row : claimsRecords) {
		        Object[] objects = row.toArray();
		        Long claimLineId = (Long) objects[0];
		        claimLineIds.add(claimLineId);
		        Integer claimLines = (Integer) objects[1];
		        claimLineNums.add(claimLines);
		        String aLevelOne = (String) objects[2];
		        String aLevelTwo = (String) objects[3];
		        String aLevelThree = (String) objects[4];
		        String aLevelFour = (String) objects[5];
		        if (isAlreadyAssigned(aLevelOne, aLevelTwo, aLevelThree, aLevelFour, processRequest)) {
		        	if(LOGGER.isDebugEnabled()) {
		        		LOGGER.debug(String.format("One or more ClaimLine already assigned for claim %s", claimNum));
		        	}
		        }                        
		    }
		}
		//Update all claimLines for CLaimNum
		BooleanBuilder builder = new BooleanBuilder();
		JPAUpdateClause update = jpaQuery.update(qClaimLineEntity);
		
		builder.and(qClaimLineEntity.claimLineId.in(claimLineIds))
		.and(qClaimLineEntity.claimNum.eq(claimNum))
		.and(qClaimLineEntity.claimLineNum.in(claimLineNums));

		ClaimDAOHelper.buildUpdateQuery(update, builder,qClaimLineEntity, processRequest, userDetails.getUserId(), userDetails.getName());
		update.set(qClaimLineEntity.updatedBy,userDetails.getUserId()).set(qClaimLineEntity.updatedTime, new Timestamp(System.currentTimeMillis()));
		count = update.where(builder.getValue()).execute();
		if(count > 0) {
			claimLineNums.forEach(lines -> successList.add(claimNum+CLAIM_LINE_MSG_FORMAT+lines));
		}else {
			claimLineNums.forEach(lines -> failedList.add(claimNum+CLAIM_LINE_MSG_FORMAT+lines));
		}
		LOGGER.info("cliam/lines %s assigned successfully {}", claimLineNums);
		return count;
	}
	@Override
	public List<Long> getClaimIds(Long claimNum) {
		BooleanBuilder builder = new BooleanBuilder();
		final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
		final QClaimLineEntity claimLine = QClaimLineEntity.claimLineEntity;
		if (claimNum > 0) {
			builder.and(claimLine.claimNum.eq(claimNum));
		}
		return (List<Long>) query.select(claimLine.claimLineId).from(claimLine).where(builder.getValue()).fetch();
	}

	@Override
	public ClaimlineListDetailsResponse getClaimLineListSummary(ClaimlineListDetailsRequest claimlineListDetailsRequest) {
	ClaimlineListDetailsResponse claimlineListDetailsResponse = null;
	List<ClaimLineBean> responseClaimLineBeanList = new ArrayList<>();
	List<Long> claimLevelClaimLineIds = claimlineListDetailsRequest.getClaimLevelClaimLineIdList();
	List<Long> claimLineLevelClaimLineIds = claimlineListDetailsRequest.getClaimlineLevelClaimLineIdList();
	final JPAQuery<ClaimLineEntity> query = new JPAQuery<>(entityManager);
	QClaimLineEntity qClaimLineEntity = QClaimLineEntity.claimLineEntity;
	BooleanBuilder builder = null;
	try{
		claimlineListDetailsResponse = new ClaimlineListDetailsResponse();
		//CLAIM_LINE level ExCode
		if(!CollectionUtils.isEmpty(claimLineLevelClaimLineIds)){
			builder = new BooleanBuilder();
			JPAQuery<ClaimLineEntity> cloneClaimLineJPAQuery = query.clone();
			//fetch all records/claimlines and add to ClaimLineBean
			builder.and(qClaimLineEntity.claimLineId.in(claimLineLevelClaimLineIds));
			List<ClaimLineEntity> claimLineLevelEntities = cloneClaimLineJPAQuery.select(qClaimLineEntity).from(qClaimLineEntity)
					.where(builder.getValue()).fetch();
			responseClaimLineBeanList.addAll(addClaimLineBeanToList(claimLineLevelEntities));
		}
		//CLAIM level ExCode
		if(!CollectionUtils.isEmpty(claimLevelClaimLineIds)){
			builder = new BooleanBuilder();
			//fetch unique claimNum and pull all records/claimlines and add to ClaimLineBean
			JPAQuery<ClaimLineEntity> cloneClaimJPAQuery = query.clone();
			List<Long> claimNumList = cloneClaimJPAQuery.select(qClaimLineEntity.claimNum).distinct().from(qClaimLineEntity)
					.where(qClaimLineEntity.claimLineId.in(claimLevelClaimLineIds)).fetch();

			builder.and(qClaimLineEntity.claimNum.in(claimNumList));
			List<ClaimLineEntity> claimLevelLineEntities = query.select(qClaimLineEntity).from(qClaimLineEntity)
					.where(builder.getValue()).fetch();
			responseClaimLineBeanList.addAll(addClaimLineBeanToList(claimLevelLineEntities));
		}
		claimlineListDetailsResponse.setClaimLines(responseClaimLineBeanList);
	}catch (Exception e){
		LOGGER.error("Exception occurred while fetching claimlines :: getClaimLineListSummary :: " +e.getMessage());
		throw e;
	}
	return claimlineListDetailsResponse;
	}
}
