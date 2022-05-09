package com.aarete.pi.helper;

import static com.aarete.pi.enums.SummaryBy.getInstanceByName;
import static com.aarete.pi.helper.ClaimServiceHelper.addExCodeDetails;
import static com.aarete.pi.util.CommonUtils.convertToZoneDateTime;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static org.springframework.util.StringUtils.hasText;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.aarete.pi.bean.ClaimLineCount;
import com.aarete.pi.bean.ClaimProcessRequest;
import com.aarete.pi.bean.ClaimRequest;
import com.aarete.pi.bean.ClaimlineComment;
import com.aarete.pi.bean.CodeType;
import com.aarete.pi.bean.EngagementFilterRequest;
import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.bean.ProviderBean;
import com.aarete.pi.bean.SummaryDTO;
import com.aarete.pi.bean.ClaimLineBean;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static com.aarete.pi.constant.WorkbenchConstants.PEND_BY_SELF;
import static com.aarete.pi.constant.WorkbenchConstants.CST_ZONE;
import static com.aarete.pi.constant.WorkbenchConstants.DATE_FORMATTER;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.ClaimlineCommentEntity;
import com.aarete.pi.entity.QClaimLineEntity;
import com.aarete.pi.enums.ClaimAction;
import com.aarete.pi.enums.ClaimStatus;
import com.aarete.pi.enums.Codes;
import com.aarete.pi.enums.Role;
import com.aarete.pi.enums.SummaryBy;
import com.aarete.pi.enums.SummaryByColumns;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;

public class ClaimDAOHelper {

	private ClaimDAOHelper() {

	}

	public static void applyAllFilters(ClaimRequest claimRequest, BooleanBuilder builder, QClaimLineEntity claimLine) {
		if (Objects.nonNull(claimRequest.getFilters())) {
			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getLobIdList())) {
				builder.and(claimLine.lobId.in(claimRequest.getFilters().getLobIdList()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getStateIdList())) {
				builder.and(claimLine.stateId.in(claimRequest.getFilters().getStateIdList()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getSubProducts())) {
				builder.and(claimLine.subProductCode.in(claimRequest.getFilters().getSubProducts()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getMemberStates())) {
				builder.and(claimLine.memberStateCode.in(claimRequest.getFilters().getMemberStates()));
			}

			if (claimRequest.getFilters().getReceivedDateStart() != null
					&& claimRequest.getFilters().getReceivedDateEnd() != null) {
				builder.and(claimLine.receivedDate.between(claimRequest.getFilters().getReceivedDateStart(),
						claimRequest.getFilters().getReceivedDateEnd()));
			}

			if (claimRequest.getFilters().getPaidDateStart() != null
					&& claimRequest.getFilters().getPaidDateEnd() != null) {
				builder.and(claimLine.paidDate.between(claimRequest.getFilters().getPaidDateStart(),
						claimRequest.getFilters().getPaidDateEnd()));
			}

			if (claimRequest.getFilters().getClaimStartDateStart() != null
					&& claimRequest.getFilters().getClaimStartDateEnd() != null) {
				builder.and(claimLine.claimStartDate.between(claimRequest.getFilters().getClaimStartDateStart(),
						claimRequest.getFilters().getClaimStartDateEnd()));
			}

			if (claimRequest.getFilters().getClaimEndDateStart() != null
					&& claimRequest.getFilters().getClaimEndDateEnd() != null) {
				builder.and(claimLine.claimEndDate.between(claimRequest.getFilters().getClaimEndDateStart(),
						claimRequest.getFilters().getClaimEndDateEnd()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getProviderIdList())) {
				builder.and(claimLine.billingProviderNumber.in(claimRequest.getFilters().getProviderIdList()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getProviderNPIList())) {
				builder.and(claimLine.billingProviderNpi.in(claimRequest.getFilters().getProviderNPIList()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getProviderIRSList())) {
				builder.and(claimLine.billingProviderIrs.in(claimRequest.getFilters().getProviderIRSList()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getProviderSpecialityList())) {
				builder.and(claimLine.billingProviderSpecialityOne.in(claimRequest.getFilters().getProviderSpecialityList()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getCodeTypes())) {
				for (CodeType codeType : claimRequest.getFilters().getCodeTypes()) {
					Codes instanceByName = Codes.getInstanceByName(codeType.getCode());
					if (instanceByName != null && !CollectionUtils.isEmpty(codeType.getCodeValues())) {
						switch (instanceByName) {
						case CPT:
							builder.and(claimLine.cptCode.in(codeType.getCodeValues()));
							break;
						case DX:
							builder.and(claimLine.diagnosisCode1.in(codeType.getCodeValues()));
							break;
						case REV:
							builder.and(claimLine.revenueCode.in(codeType.getCodeValues()));
							break;
						default:
							break;
						}
					}
				}

			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getExCodes())) {
				builder.and(claimLine.exCode.in(claimRequest.getFilters().getExCodes()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getClaimFormTypeIdList())) {
				builder.and(claimLine.claimFormTypeCode.in(claimRequest.getFilters().getClaimFormTypeIdList()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getBillTypeCodes())) {
				builder.and(claimLine.billTypeCode.in(claimRequest.getFilters().getBillTypeCodes()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getPos())) {
				builder.and(claimLine.posId.in(claimRequest.getFilters().getPos()));
			}

			if (claimRequest.getFilters().getBilledAmountStart() > -1
					&& claimRequest.getFilters().getBilledAmountEnd() > 0) {
				builder.and(claimLine.lineBilledAmount.between(claimRequest.getFilters().getBilledAmountStart(),
						claimRequest.getFilters().getBilledAmountEnd()));
			}

			if (claimRequest.getFilters().getAllowedAmountStart() > -1
					&& claimRequest.getFilters().getAllowedAmountEnd() > 0) {
				builder.and(claimLine.lineAllowedAmount.between(claimRequest.getFilters().getAllowedAmountStart(),
						claimRequest.getFilters().getAllowedAmountEnd()));
			}

			if (claimRequest.getFilters().getPaidAmountStart() > -1
					&& claimRequest.getFilters().getPaidAmountEnd() > 0) {
				builder.and(claimLine.linePaidAmount.between(claimRequest.getFilters().getPaidAmountStart(),
						claimRequest.getFilters().getPaidAmountEnd()));
			}

			if (claimRequest.getFilters().getOpportunityAmountStart() > -1
					&& claimRequest.getFilters().getOpportunityAmountEnd() > 0) {
				builder.and(
						claimLine.lineOpportunityAmount.between(claimRequest.getFilters().getOpportunityAmountStart(),
								claimRequest.getFilters().getOpportunityAmountEnd()));
			}

			if (claimRequest.getFilters().getModelScoreStart() > 0
					&& claimRequest.getFilters().getModelScoreEnd() > 0) {
				builder.and(claimLine.modelScore.between(claimRequest.getFilters().getModelScoreStart(),
						claimRequest.getFilters().getModelScoreEnd()));
			}

			if (claimRequest.getFilters().getBatchStartDate() != null
					&& claimRequest.getFilters().getBatchEndDate() != null) {
				builder.and(claimLine.batchDate.between(claimRequest.getFilters().getBatchStartDate(),
						claimRequest.getFilters().getBatchEndDate()));
			}

			if (!CollectionUtils.isEmpty(claimRequest.getFilters().getAuditList())) {
				builder.and(claimLine.batchId.in(claimRequest.getFilters().getAuditList()));
			}

			if (!CollectionUtils.isEmpty((claimRequest.getFilters().getPillarIds()))) {
				builder.and(claimLine.pillarId.in(claimRequest.getFilters().getPillarIds()));
			}

			if (claimRequest.getFilters().getConfidenceScore() > 0.0f) {
				builder.and(claimLine.confidenceScore.eq(claimRequest.getFilters().getConfidenceScore()));
			}

			if (hasText(claimRequest.getFilters().getClaimNum())) {
				builder.and(claimLine.claimNum.like("%" + claimRequest.getFilters().getClaimNum() + "%"));
			}

		}

		if(ClaimStatus.GROUP_QUEUE.name().equals(claimRequest.getFilters().getClaimStatus())) {
			applyGroupFilter(claimRequest.getEngagementFilters(), builder, claimLine);
		} else {
			applyRoleFilter(claimRequest.getEngagementFilters(), builder, claimLine);
		}

		applyEngagementFilters(claimRequest.getEngagementFilters(), builder, claimLine);
	}

	private static void applyRoleFilter(EngagementFilterRequest engagementFilters, BooleanBuilder builder, QClaimLineEntity claimLine) {
		if (hasText(engagementFilters.getEngagementRole())) {
			Role role = Role.getInstanceByName(engagementFilters.getEngagementRole());
			switch (role) {
				case AARETE_USER:
					builder.and(claimLine.approverLevelOne.eq(engagementFilters.getLoggedInUserEmailId()));
					break;
				case AARETE_MANAGER:
					builder.and(claimLine.approverLevelTwo.eq(engagementFilters.getLoggedInUserEmailId()));
					break;
				case CLIENT_USER:
					builder.and(claimLine.approverLevelThree.eq(engagementFilters.getLoggedInUserEmailId()));
					break;
				case CLIENT_MANAGER:
					builder.and(claimLine.approverLevelFour.eq(engagementFilters.getLoggedInUserEmailId()));
					break;
				default:
					break;
			}
		}
	}

	private static void applyGroupFilter(EngagementFilterRequest engagementFilters, BooleanBuilder builder, QClaimLineEntity claimLine) {
		if (hasText(engagementFilters.getEngagementRole())) {
			Role role = Role.getInstanceByName(engagementFilters.getEngagementRole());
			switch (role) {
				case AARETE_USER:
					builder.and(claimLine.approverLevelOneGroup.in(engagementFilters.getGroupNames()));
					builder.and(claimLine.approverLevelOne.isNull());
					break;
				case AARETE_MANAGER:
					builder.and(claimLine.approverLevelTwoGroup.in(engagementFilters.getGroupNames()));
					builder.and(claimLine.approverLevelTwo.isNull());
					break;
				case CLIENT_USER:
					builder.and(claimLine.approverLevelThreeGroup.in(engagementFilters.getGroupNames()));
					builder.and(claimLine.approverLevelThree.isNull());
					break;
				case CLIENT_MANAGER:
					builder.and(claimLine.approverLevelFourGroup.in(engagementFilters.getGroupNames()));
					builder.and(claimLine.approverLevelFour.isNull());
					break;
				default:
					break;
			}
		}
	}

	public static void applyEngagementFilters(EngagementFilterRequest engagementFilters, BooleanBuilder builder,
			QClaimLineEntity claimLine) {
		if (Objects.nonNull(engagementFilters)) {

			builder.and(claimLine.claimStatusCode.notLike("%CLOSED%").or(claimLine.claimStatusCode.isNull()));

			if (hasText(engagementFilters.getEngagementId())) {
				builder.and(claimLine.engagementId.eq(engagementFilters.getEngagementId()));
			}

			if (!CollectionUtils.isEmpty(engagementFilters.getPiPillarList())) {
				builder.and(claimLine.pillarId.in(engagementFilters.getPiPillarList()));
			}

			if (!CollectionUtils.isEmpty(engagementFilters.getLobList())) {
				builder.and(claimLine.lobId.in(engagementFilters.getLobList()));
			}

			if (!CollectionUtils.isEmpty(engagementFilters.getStateList())) {
				builder.and(claimLine.stateId.in(engagementFilters.getStateList()));
			}
		}
	}

	public static Optional<List<Tuple>> groupBySummaryQuery(Predicate predicate, JPAQuery<ClaimLineEntity> query,
			QClaimLineEntity claimLine, SummaryBy summaryBy, ClaimRequest claimRequest) {
		Optional<List<Tuple>> result = Optional.empty();
		JPAQuery<ClaimLineEntity> where = query.from(claimLine).where(predicate);

		if (summaryBy != null) {
			switch (summaryBy) {
				case EDIT:
					// TODO optimize this cases to reuse common code
					return Optional.ofNullable(where.groupBy(claimLine.edit).select(claimLine.edit,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case EX_CODE:
					return Optional.ofNullable(where.groupBy(claimLine.exCode).select(claimLine.exCode,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case PROVIDER_TIN:
					return Optional.ofNullable(where.groupBy(claimLine.billingProviderIrs).select(claimLine.billingProviderIrs,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case PROVIDER_NPI:
					return Optional.ofNullable(where.groupBy(claimLine.billingProviderNpi).select(claimLine.billingProviderNpi,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case PROVIDER_NAME:
					return Optional.ofNullable(where.groupBy(claimLine.billingProviderNumber, claimLine.billingProviderFullName).select(claimLine.billingProviderNumber, claimLine.billingProviderFullName,
							getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case CPT_CODE:
					return Optional.ofNullable(where.groupBy(claimLine.cptCode).select(claimLine.cptCode,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case REV_CODE:
					return Optional.ofNullable(where.groupBy(claimLine.revenueCode).select(claimLine.revenueCode,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case BILL_TYPE:
					return Optional.ofNullable(where.groupBy(claimLine.billTypeCode).select(claimLine.billTypeCode,
							getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case POS:
					return Optional.ofNullable(where.groupBy(claimLine.posId).select(claimLine.posId,
							getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case BATCH:
					return Optional.ofNullable(where.groupBy(claimLine.batchId).select(claimLine.batchId,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case DATE_OF_SERVICE:
					return Optional.ofNullable(where.groupBy(claimLine.claimStartDate).select(claimLine.claimStartDate,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case CLAIM_PAID_DATE:
					return Optional.ofNullable(where.groupBy(claimLine.paidDate).select(claimLine.paidDate,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				case CLAIM_RECEIVE_DATE:
					return Optional.ofNullable(where.groupBy(claimLine.receivedDate).select(claimLine.receivedDate,
									getColumnName(claimLine, claimRequest, true), getColumnName(claimLine, claimRequest, false))
							.limit(claimRequest.getSummaryBy().getRecordCount()).offset(0)
							.fetch());
				default:
					break;
			}
		}

		return result;
	}

	public static Optional<List<Tuple>> groupByClaimStatus(Predicate predicate, JPAQuery<ClaimLineEntity> query,
															QClaimLineEntity claimLine, ClaimRequest claimRequest) {
		if (hasText(claimRequest.getEngagementFilters().getEngagementRole())) {
			Role role = Role.getInstanceByName(claimRequest.getEngagementFilters().getEngagementRole());
			JPAQuery<ClaimLineEntity> where = query.from(claimLine).where(predicate);
			switch (role) {
				case AARETE_USER:
					return Optional.ofNullable(where.groupBy(claimLine.claimStatusLevelOne).select(claimLine.claimStatusLevelOne, claimLine.claimStatusLevelOne.count()).fetch());
				case AARETE_MANAGER:
					return Optional.ofNullable(where.groupBy(claimLine.claimStatusLevelTwo).select(claimLine.claimStatusLevelTwo, claimLine.claimStatusLevelTwo.count()).fetch());
				case CLIENT_USER:
					return Optional.ofNullable(where.groupBy(claimLine.claimStatusLevelThree).select(claimLine.claimStatusLevelThree, claimLine.claimStatusLevelThree.count()).fetch());
				case CLIENT_MANAGER:
					return Optional.ofNullable(where.groupBy(claimLine.claimStatusLevelFour).select(claimLine.claimStatusLevelFour, claimLine.claimStatusLevelFour.count()).fetch());
				default:
					return Optional.empty();
			}
		}
		return Optional.empty();
	}

	private static NumberExpression<?> getColumnName(QClaimLineEntity claimLine, ClaimRequest claimRequest,
			boolean isColumnOne) {
		if (claimRequest.getSummaryBy().getColumnOneName() != null) {
			SummaryByColumns summaryByColumns;
			if (isColumnOne) {
				summaryByColumns = SummaryByColumns.getInstanceByName(claimRequest.getSummaryBy().getColumnOneName());
			} else {
				summaryByColumns = SummaryByColumns.getInstanceByName(claimRequest.getSummaryBy().getColumnTwoName());
			}
			switch (summaryByColumns) {
			case CLAIMS:
				return claimLine.claimNum.countDistinct();
			case CLAIM_LINES:
				return claimLine.claimLineId.count();
			case UNITS:
				return claimLine.units.count();
			case PAID:
				return claimLine.linePaidAmount.sum();
			case OPPORTUNITY:
				return claimLine.lineOpportunityAmount.sum();
			case BILLED:
				return claimLine.lineBilledAmount.sum();
			default:
				return null;
			}
		}
		return null;
	}

	public static Optional<List<SummaryDTO>> transformToDTO(List<Tuple> listOfRecords) {
		List<SummaryDTO> summaryDTOs = new ArrayList<>();
		listOfRecords.forEach(tuple -> {
			Object[] objects = tuple.toArray();
			if(objects.length == 4) {
				summaryDTOs.add(new SummaryDTO(String.valueOf(objects[0]), String.valueOf(objects[1]), (String.valueOf(objects[2])), (String.valueOf(objects[3]))));
			} else {
				summaryDTOs.add(new SummaryDTO(String.valueOf(objects[0]), String.valueOf(objects[0]), (String.valueOf(objects[1])), (String.valueOf(objects[2]))));
			}

		});
		return Optional.of(summaryDTOs);
	}

	public static List<ProviderBean> transformToProviderBean(List<Tuple> claimLineEntities) {
		List<ProviderBean> providerBeans = new ArrayList<>();
		claimLineEntities.forEach(tuple -> {
			Object[] objects = tuple.toArray();
			providerBeans.add(new ProviderBean(String.valueOf(objects[0]), String.valueOf(objects[1]), (Long) objects[2], (Long) objects[3]));

		});
		return providerBeans;
	}

	public static List<ClaimlineComment> transformToComments(List<Tuple> tuples) {
		return tuples.stream().map(tuple -> {
			Object[] objects = tuple.toArray();
			ClaimlineCommentEntity claimLineCommentEntity = (ClaimlineCommentEntity) objects[0];
			ClaimlineComment claimlineComment = new ClaimlineComment();
			claimlineComment.setComment(claimLineCommentEntity.getComment());
			//Converting timestamp to CST formate mm.dd.yyyy  hh:mm
			String commentDateTime = convertToZoneDateTime(claimLineCommentEntity.getCommentTime(),
					CST_ZONE, DATE_FORMATTER);
			claimlineComment.setCommentTimestamp(hasText(commentDateTime) ? 
					commentDateTime+" "+CST_ZONE : commentDateTime);
			claimlineComment.setCommentTime(claimLineCommentEntity.getCommentTime());
			claimlineComment.setClaimLineId(claimLineCommentEntity.getClaimLineId());
			claimlineComment.setClaimNum((Long) objects[1]);
			claimlineComment.setClaimLineNum((Integer)objects[2]);
			claimlineComment.setUserId(claimLineCommentEntity.getUserId());
			claimlineComment.setUserName((String) objects[3]);
			return claimlineComment;
		}).collect(Collectors.toList());
	}

	public static ClaimLineCount setCount(Tuple tuple, ClaimLineCount claimLineCount) {
		Object[] objects = tuple.toArray();
		ClaimStatus claimStatus = ClaimStatus.getInstanceByName(String.valueOf(objects[0]));
		long total;
		switch (claimStatus) {
			case GROUP_QUEUE:
				claimLineCount.setGroupQueueCount((Long) objects[1]);
				total = claimLineCount.getTotalCountExcludingClosed() + claimLineCount.getGroupQueueCount();
				claimLineCount.setTotalCountExcludingClosed(total);
				break;
			case MY_QUEUE:
				claimLineCount.setMyQueueCount((Long) objects[1]);
				total = claimLineCount.getTotalCountExcludingClosed() + claimLineCount.getMyQueueCount();
				claimLineCount.setTotalCountExcludingClosed(total);
				break;
			case PEND:
				claimLineCount.setPendCount((Long) objects[1]);
				total = claimLineCount.getTotalCountExcludingClosed() + claimLineCount.getPendCount();
				claimLineCount.setTotalCountExcludingClosed(total);
				break;
			case WAITING:
				claimLineCount.setWaitingCount((Long) objects[1]);
				total = claimLineCount.getTotalCountExcludingClosed() + claimLineCount.getWaitingCount();
				claimLineCount.setTotalCountExcludingClosed(total);
				break;
			default:
				break;
		}
		return claimLineCount;
	}

	public static Optional<List<IdNameBean>> transformToBatchIdNameBean(List<Long> batches) {
		List<IdNameBean> idNameBeans = new ArrayList<>();
		batches.forEach(batch -> idNameBeans.add(new IdNameBean(String.valueOf(batch), String.valueOf(batch), String.valueOf(batch))));
		return Optional.of(idNameBeans);
	}

	public static void applySummaryByFilter(ClaimRequest claimRequest, BooleanBuilder builder, QClaimLineEntity claimLine) {
		SummaryBy summaryBy = getInstanceByName(claimRequest.getSummaryBy().getSummaryBy());

		if (summaryBy != null && hasText(claimRequest.getSummaryBy().getSummaryByValue())) {
			switch (summaryBy) {
				case EDIT:
					builder.and(claimLine.edit.eq(claimRequest.getSummaryBy().getSummaryByValue()));
					break;
				case EX_CODE:
					builder.and(claimLine.exCode.eq(claimRequest.getSummaryBy().getSummaryByValue()));
					break;
				case PROVIDER_TIN:
					builder.and(claimLine.billingProviderIrs.eq(parseLong(claimRequest.getSummaryBy().getSummaryByValue())));
					break;
				case PROVIDER_NPI:
					builder.and(claimLine.billingProviderNpi.eq(parseLong(claimRequest.getSummaryBy().getSummaryByValue())));
					break;
				case PROVIDER_NAME:
					builder.and(claimLine.billingProviderNumber.eq(claimRequest.getSummaryBy().getSummaryByValue()));
					break;
				case CPT_CODE:
					builder.and(claimLine.cptCode.eq(claimRequest.getSummaryBy().getSummaryByValue()));
					break;
				case REV_CODE:
					builder.and(claimLine.revenueCode.eq(claimRequest.getSummaryBy().getSummaryByValue()));
					break;
				case BILL_TYPE:
					builder.and(claimLine.billTypeCode.eq(claimRequest.getSummaryBy().getSummaryByValue()));
					break;
				case POS:
					builder.and(claimLine.posId.eq(parseInt(claimRequest.getSummaryBy().getSummaryByValue())));
					break;
				case BATCH:
					builder.and(claimLine.batchId.eq(parseLong(claimRequest.getSummaryBy().getSummaryByValue())));
					break;
				case DATE_OF_SERVICE:
					Date claimStartDate = Date.valueOf(claimRequest.getSummaryBy().getSummaryByValue());
					builder.and(claimLine.claimStartDate.eq(claimStartDate));
					break;
				case CLAIM_PAID_DATE:
					Date paidDate = java.sql.Date.valueOf(claimRequest.getSummaryBy().getSummaryByValue());
					builder.and(claimLine.paidDate.eq(paidDate));
					break;
				case CLAIM_RECEIVE_DATE:
					java.sql.Date receivedDate = java.sql.Date.valueOf(claimRequest.getSummaryBy().getSummaryByValue());
					builder.and(claimLine.receivedDate.eq(receivedDate));
					break;
				default:
					break;
			}
		}
	}

	public static void applyClaimStatusFilter(ClaimRequest claimRequest, BooleanBuilder builder, QClaimLineEntity claimLine) {
		if (hasText(claimRequest.getEngagementFilters().getEngagementRole())
				&& hasText(claimRequest.getFilters().getClaimStatus())) {
			Role role = Role.getInstanceByName(claimRequest.getEngagementFilters().getEngagementRole());
			switch (role) {
				case AARETE_USER:
					builder.and(claimLine.claimStatusLevelOne.eq(claimRequest.getFilters().getClaimStatus()));
					break;
				case AARETE_MANAGER:
					builder.and(claimLine.claimStatusLevelTwo.eq(claimRequest.getFilters().getClaimStatus()));
					break;
				case CLIENT_USER:
					builder.and(claimLine.claimStatusLevelThree.eq(claimRequest.getFilters().getClaimStatus()));
					break;
				case CLIENT_MANAGER:
					builder.and(claimLine.claimStatusLevelFour.eq(claimRequest.getFilters().getClaimStatus()));
					break;
				default:
					break;
			}
		}
	}
	
	public static void buildUpdateQuery(JPAUpdateClause update,BooleanBuilder builder, QClaimLineEntity claimLine,
			ClaimProcessRequest processRequest, String userId, String userName) {
		if (hasText(processRequest.getEngagementRole())) {
			String claimStatus = null;
			builder.and(claimLine.claimStatusCode.notLike("%CLOSED%").or(claimLine.claimStatusCode.isNull()));
			Role role = Role.getInstanceByName(processRequest.getEngagementRole());
			switch (role) {
				case AARETE_USER:
					if(ClaimAction.ASSIGN.name().equalsIgnoreCase(processRequest.getActionTaken())) {
						claimStatus = ClaimStatus.MY_QUEUE.name();
						update.set(claimLine.aareteAssignedUser, userName);
						builder.and(claimLine.approverLevelOne.isNull());
					}else if(ClaimAction.PEND.name().equalsIgnoreCase(processRequest.getActionTaken())){
						claimStatus = ClaimStatus.PEND.name();
						builder.and(claimLine.claimStatusLevelOne.eq(ClaimStatus.MY_QUEUE.name()));
						update.set(claimLine.aareteUserPendReason, PEND_BY_SELF);
					}
					update.set(claimLine.approverLevelOne, userId);
					update.set(claimLine.claimStatusLevelOne, claimStatus);
					break;
				case AARETE_MANAGER:
					if(ClaimAction.ASSIGN.name().equalsIgnoreCase(processRequest.getActionTaken())) {
						claimStatus = ClaimStatus.MY_QUEUE.name();
						builder.and(claimLine.approverLevelTwo.isNull());
					}else if(ClaimAction.PEND.name().equalsIgnoreCase(processRequest.getActionTaken())){
						claimStatus = ClaimStatus.PEND.name();
						builder.and(claimLine.claimStatusLevelTwo.eq(ClaimStatus.MY_QUEUE.name()));
						update.set(claimLine.aareteManagerPendReason, PEND_BY_SELF);
					}
					update.set(claimLine.approverLevelTwo, userId);
					update.set(claimLine.claimStatusLevelTwo, claimStatus);
					break;
				case CLIENT_USER:
					if(ClaimAction.ASSIGN.name().equalsIgnoreCase(processRequest.getActionTaken())) {
						claimStatus = ClaimStatus.MY_QUEUE.name();
						update.set(claimLine.clientAssignedUser, userName);
						builder.and(claimLine.approverLevelThree.isNull());
					}else if(ClaimAction.PEND.name().equalsIgnoreCase(processRequest.getActionTaken())){
						claimStatus = ClaimStatus.PEND.name();
						builder.and(claimLine.claimStatusLevelThree.eq(ClaimStatus.MY_QUEUE.name()));
						update.set(claimLine.clientUserPendReason, PEND_BY_SELF);
					}
					update.set(claimLine.approverLevelThree, userId);
					update.set(claimLine.claimStatusLevelThree, claimStatus);
					break;
				case CLIENT_MANAGER:
					if(ClaimAction.ASSIGN.name().equalsIgnoreCase(processRequest.getActionTaken())) {
						claimStatus = ClaimStatus.MY_QUEUE.name();
						builder.and(claimLine.approverLevelFour.isNull());
					}else if(ClaimAction.PEND.name().equalsIgnoreCase(processRequest.getActionTaken())){
						claimStatus = ClaimStatus.PEND.name();
						builder.and(claimLine.claimStatusLevelFour.eq(ClaimStatus.MY_QUEUE.name()));
						update.set(claimLine.clientManagerPendReason, PEND_BY_SELF);
					}
					update.set(claimLine.approverLevelFour, userId);
					update.set(claimLine.claimStatusLevelFour, claimStatus);
					break;
				default:
					break;
			}
		}
	}

	public static boolean isAlreadyAssigned(String aLevelOne, String aLevelTwo, String aLevelThree, String aLevelFour,
			ClaimProcessRequest processRequest){
		if (hasText(processRequest.getEngagementRole())) {
			Role role = Role.getInstanceByName(processRequest.getEngagementRole());
			switch (role) {
				case AARETE_USER:
					if(StringUtils.hasText(aLevelOne)) //Checking approverLevel1 - Is ClaimLine already assigned? 
						return true;
					break;
				case AARETE_MANAGER:
					if(StringUtils.hasText(aLevelTwo)) //Checking approverLevel2 - Is ClaimLine already assigned? 
						return true;
					break;
				case CLIENT_USER:
					if(StringUtils.hasText(aLevelThree)) //Checking approverLevel3 - Is ClaimLine already assigned? 
						return true;
					break;
				case CLIENT_MANAGER:
					if(StringUtils.hasText(aLevelFour)) //Checking approverLevel4 - Is ClaimLine already assigned? 
						return true;
					break;
				default:
					break;
			}
		}
		return false;
	}

	/**
	 *
	 * @param claimLineEntities
	 * @return
	 */
	public static List<ClaimLineBean> addClaimLineBeanToList(List<ClaimLineEntity> claimLineEntities){
		List<ClaimLineBean> responseClaimLineBeanList = new ArrayList<>();
		for (ClaimLineEntity claimLineEntity : claimLineEntities) {
			ClaimLineBean claimLineBean = new ClaimLineBean();
			BeanUtils.copyProperties(claimLineEntity, claimLineBean);
			addExCodeDetails(claimLineEntity, claimLineBean);
			responseClaimLineBeanList.add(claimLineBean);
		}
		return responseClaimLineBeanList;
	}

}
