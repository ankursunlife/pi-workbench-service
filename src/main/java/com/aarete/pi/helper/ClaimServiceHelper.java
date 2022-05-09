package com.aarete.pi.helper;

import static com.aarete.pi.enums.Role.AARETE_MANAGER;
import static com.aarete.pi.enums.Role.CLIENT_MANAGER;
import static com.aarete.pi.enums.SummaryByColumns.getInstanceByName;
import static com.aarete.pi.util.WBConstants.DESC;
import static java.util.Comparator.comparing;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.aarete.pi.bean.ClaimLineBean;
import com.aarete.pi.bean.ClaimRequest;
import com.aarete.pi.bean.ClaimSummary;
import com.aarete.pi.bean.DiagnosisBean;
import com.aarete.pi.bean.ExCodeBean;
import com.aarete.pi.bean.MemberBean;
import com.aarete.pi.bean.ProviderBean;
import com.aarete.pi.bean.SortingBean;
import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.ClaimlineDiagnosisEntity;
import com.aarete.pi.entity.ClaimlineExCodeEntity;
import com.aarete.pi.entity.MemberEntity;
import com.aarete.pi.entity.ProviderEntity;
import com.aarete.pi.entity.QClaimLineEntity;
import com.aarete.pi.enums.ClaimLineSort;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.aarete.pi.enums.SummaryByColumns;

@Component
public class ClaimServiceHelper {

	public static List<ClaimSummary> sortResult(List<ClaimSummary> summaryDTOs, ClaimRequest claimRequest) {
		String sortBy = claimRequest.getSummaryBy().getSortBy();
		String sortType = claimRequest.getSummaryBy().getSortType();

		if (hasText(sortBy) && hasText(sortType)) {
			Comparator<ClaimSummary> comparator = Comparator.comparing(e -> Integer.parseInt(e.getColumnOneValue()));
			SummaryByColumns summaryByColumns = getInstanceByName(sortBy);
			switch (summaryByColumns) {
				case CLAIMS:
				case CLAIM_LINES:
				case UNITS:
					comparator = getColumnOneComparator(sortType);
					break;
				case PAID:
				case OPPORTUNITY:
				case BILLED:
					comparator = getColumnTwoComparator(sortType);
					break;
				default:
					break;
			}
			return summaryDTOs.stream().sorted(comparator).collect(Collectors.toList());
		}
		return summaryDTOs;
	}

	private static Comparator<ClaimSummary> getColumnOneComparator(String sortByType) {
		Comparator<ClaimSummary> comparator = Comparator.comparing(e -> Integer.parseInt(e.getColumnOneValue()));
		return "DESC".equals(sortByType) ? comparator.reversed() : comparator;
	}

	private static Comparator<ClaimSummary> getColumnTwoComparator(String sortByType) {
		Comparator<ClaimSummary> comparator = Comparator.comparing(e -> Double.parseDouble(e.getColumnTwoValue()));
		return "DESC".equals(sortByType) ? comparator.reversed() : comparator;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public static List<ClaimLineEntity> getClaimLineEntities(List<ClaimLineBean> claimLineBeans) {
		List<ClaimLineEntity> claimLineEntities = new ArrayList<>();
		for (ClaimLineBean claimLineBean : claimLineBeans) {
			ClaimLineEntity entity = new ClaimLineEntity();
			BeanUtils.copyProperties(claimLineBean, entity);
			List<ClaimlineExCodeEntity> claimLineExCodeEntities = new ArrayList<>();
			for (ExCodeBean exCodeBean : claimLineBean.getExCodeBeanList()) {
				ClaimlineExCodeEntity claimlineExCodeEntity = new ClaimlineExCodeEntity();
				BeanUtils.copyProperties(exCodeBean, claimlineExCodeEntity);
				claimlineExCodeEntity.setClaimLineEntity(entity);
				claimlineExCodeEntity.setClaimNum(entity.getClaimNum());
				claimLineExCodeEntities.add(claimlineExCodeEntity);
			}
			entity.setExCodeBeanList(claimLineExCodeEntities);

			if(claimLineBean.getMember() != null) {
				MemberEntity memberEntity = new MemberEntity();
				BeanUtils.copyProperties(claimLineBean.getMember(), memberEntity);
				entity.setMember(memberEntity);
			}

			if(claimLineBean.getProvider() != null) {
				ProviderEntity providerEntity = new ProviderEntity();
				BeanUtils.copyProperties(claimLineBean.getProvider(), providerEntity);
				entity.setProvider(providerEntity);
			}

			List<ClaimlineDiagnosisEntity> diagnosisEntities = new ArrayList<>();
			for (DiagnosisBean diagnosisBean : claimLineBean.getDiagnosisBeanList()) {
				ClaimlineDiagnosisEntity claimlineDiagnosisEntity = new ClaimlineDiagnosisEntity();
				BeanUtils.copyProperties(diagnosisBean, claimlineDiagnosisEntity);
				claimlineDiagnosisEntity.setClaimLineEntity(entity);
				claimlineDiagnosisEntity.setClaimNum(entity.getClaimNum());
				claimlineDiagnosisEntity.setClaimLineNum(entity.getClaimLineNum());
				claimlineDiagnosisEntity.setClaimStatusCode(entity.getClaimStatusCode());
				diagnosisEntities.add(claimlineDiagnosisEntity);
			}
			entity.setDiagnosisBeanList(diagnosisEntities);

			claimLineEntities.add(entity);
		}
		return claimLineEntities;
	}

	public static OrderSpecifier<?> sortClaimLineListResult(final QClaimLineEntity claimLine,
			ClaimRequest claimRequest) {

		String sortBy = "";
		String sortByType = DESC;
		if (!Objects.isNull(claimRequest.getSortingAndPagination()) && !Objects.isNull(claimRequest.getSortingAndPagination().getSortingBeanList()) && !claimRequest.getSortingAndPagination().getSortingBeanList().isEmpty()) {
			List<SortingBean> sortingBeanList = claimRequest.getSortingAndPagination().getSortingBeanList();
			sortBy = sortingBeanList.get(0).getSortBy();
			sortByType = sortingBeanList.get(0).getSortType();
		}
		ClaimLineSort claimLineSort = ClaimLineSort.getInstanceByName(sortBy);
		boolean isDesc = DESC.equalsIgnoreCase(sortByType);
		switch (claimLineSort) {
		case PROV_NAME:
			return isDesc ? claimLine.billingProviderFullName.desc() : claimLine.billingProviderFullName.asc();
		case USER_ACTION:
			if(AARETE_MANAGER.name().equalsIgnoreCase(claimRequest.getEngagementFilters().getEngagementRole())) {
				return isDesc ? claimLine.approverOneAction.desc() : claimLine.approverOneAction.asc();
			} else if(CLIENT_MANAGER.name().equalsIgnoreCase(claimRequest.getEngagementFilters().getEngagementRole())) {
				return isDesc ? claimLine.approverThreeAction.desc() : claimLine.approverThreeAction.asc();
			}
		case CLAIM_NUM:
			return isDesc ? claimLine.claimNum.desc() : claimLine.claimNum.asc();
		case CLAIM_LINE_NUM:
			return isDesc ? claimLine.claimLineNum.desc() : claimLine.claimLineNum.asc();
		case OPP_AMT:
			return isDesc ? claimLine.lineOpportunityAmount.desc() : claimLine.lineOpportunityAmount.asc();
		case EDIT:
			return isDesc ? claimLine.edit.desc() : claimLine.edit.asc();
		case AARETE_EX_CODE:
			return isDesc ? claimLine.exCode.desc() : claimLine.exCode.asc();
		case CLAIM_FROM_TYPE:
			return isDesc ? claimLine.claimFormTypeCode.desc() : claimLine.claimFormTypeCode.asc();
		case BILL_TYPE_CODE:
			return isDesc ? claimLine.billTypeCode.desc() : claimLine.billTypeCode.asc();
		case POS:
			return isDesc ? claimLine.posId.desc() : claimLine.posId.asc();
		case CPT_CODE:
			return isDesc ? claimLine.cptCode.desc() : claimLine.cptCode.asc();
		case REV_CODE:
			return isDesc ? claimLine.revenueCode.desc() : claimLine.revenueCode.asc();
		case DRG_CODE:
			return isDesc ? claimLine.drgCode.desc() : claimLine.drgCode.asc();
		case LOB:
			return isDesc ? claimLine.lobId.desc() : claimLine.lobId.asc();
		case BILLED_AMT:
			return isDesc ? claimLine.lineBilledAmount.desc() : claimLine.lineBilledAmount.asc();
		case ALLOWED_AMT:
			return isDesc ? claimLine.lineAllowedAmount.desc() : claimLine.lineAllowedAmount.asc();
		case PAID_AMT:
			return isDesc ? claimLine.linePaidAmount.desc() : claimLine.linePaidAmount.asc();
		case UNITS_PAID:
			return isDesc ? claimLine.units.desc() : claimLine.units.asc();
		case STATE:
			return isDesc ? claimLine.stateId.desc() : claimLine.stateId.asc();
		case PROV_TIN:
			return isDesc ? claimLine.billingProviderIrs.desc() : claimLine.billingProviderIrs.asc();
		case PROV_NPI:
			return isDesc ? claimLine.billingProviderNpi.desc() : claimLine.billingProviderNpi.asc();
		case PROV_SPECIALTY:
			return isDesc ? claimLine.billingProviderSpecialityOne.desc() : claimLine.billingProviderSpecialityOne.asc();
		case CLAIM_START_DATE:
			return isDesc ? claimLine.claimStartDate.desc() : claimLine.claimStartDate.asc();
		case CLAIM_END_DATE:
			return isDesc ? claimLine.claimEndDate.desc() : claimLine.claimEndDate.asc();
		case RECEIVED_DATE:
			return isDesc ? claimLine.receivedDate.desc() : claimLine.receivedDate.asc();
		case PAID_DATE:
			return isDesc ? claimLine.paidDate.desc() : claimLine.paidDate.asc();
		case MEMBER_ID:
			return isDesc ? claimLine.memberId.desc() : claimLine.memberId.asc();
		case PRIMARY_DX_CODE:
			return isDesc ? claimLine.diagnosisCode1.desc() : claimLine.diagnosisCode1.asc();
		case BATCH_DATE:
			return isDesc ? claimLine.batchDate.desc() : claimLine.batchDate.asc();
		case BATCH_NO:
			return isDesc ? claimLine.batchId.desc() : claimLine.batchId.asc();
		case EDIT_TYPE:
			return isDesc ? claimLine.editType.desc() : claimLine.editType.asc();
		case PRIORITIZATION_SCORE:
			return isDesc ? claimLine.prioritizationScore.desc() : claimLine.prioritizationScore.asc();
		case ASSIGNER:
			return isDesc ? claimLine.assigner.desc() : claimLine.assigner.asc();
		case OVERRIDE_REASON:
			return isDesc ? claimLine.overrideReason.desc() : claimLine.overrideReason.asc();
		case PEND_REASON:
			return isDesc ? claimLine.pendReason.desc() : claimLine.pendReason.asc();
		case CPT_DESC:
			return isDesc ? claimLine.cptDesc.desc() : claimLine.cptDesc.asc();
		case REVENUE_DESC:
			return isDesc ? claimLine.revenueDesc.desc() : claimLine.revenueDesc.asc();
		case DIAGNOSIS1_DESC:
			return isDesc ? claimLine.diagnosis1Desc.desc() : claimLine.diagnosis1Desc.asc();
		case MULTI_LINE_EDIT:
			return isDesc ? claimLine.editType.desc() : claimLine.editType.asc();
		case MODIFIER:
			return isDesc ? claimLine.modifier.desc() : claimLine.modifier.asc();
		case P_SCORE:
			return isDesc ? claimLine.prioritizationScore.desc() : claimLine.prioritizationScore.asc();
		case AARETE_ASSIGNED_USER:
			return isDesc ? claimLine.aareteAssignedUser.desc() : claimLine.aareteAssignedUser.asc();
		case CLIENT_ASSIGNED_USER:
			return isDesc ? claimLine.clientAssignedUser.desc() : claimLine.clientAssignedUser.asc();
		default:
			return claimLine.billingProviderFullName.asc();
		}
	}

	private static List<ClaimLineBean> getCollect(List<ClaimLineBean> claimLineBeans,
			Comparator<ClaimLineBean> comparator) {
		return claimLineBeans.stream().sorted(comparator).collect(Collectors.toList());
	}

	public static void addExCodeDetails(ClaimLineEntity claimLineEntity, ClaimLineBean claimLineBean) {
		List<ExCodeBean> exBeanList = new ArrayList<>();
		for (ClaimlineExCodeEntity claimlineExCodeEntity : claimLineEntity.getExCodeBeanList()) {
			ExCodeBean exCodeBean = new ExCodeBean();
			BeanUtils.copyProperties(claimlineExCodeEntity, exCodeBean);
			exBeanList.add(exCodeBean);
		}
		claimLineBean.setExCodeBeanList(exBeanList);
	}

	public static void addProviderMemberDiagnosisDetails(ClaimLineEntity claimLineEntity, ClaimLineBean claimLineBean) {
		List<DiagnosisBean> diagnosisBeanList = new ArrayList<>();
		for (ClaimlineDiagnosisEntity claimLineExCodeEntity : claimLineEntity.getDiagnosisBeanList()) {
			DiagnosisBean diagnosisBean = new DiagnosisBean();
			BeanUtils.copyProperties(claimLineExCodeEntity, diagnosisBean);
			diagnosisBeanList.add(diagnosisBean);
		}
		claimLineBean.setDiagnosisBeanList(diagnosisBeanList);

		if(claimLineEntity.getMember() != null) {
			MemberBean memberBean = new MemberBean();
			BeanUtils.copyProperties(claimLineEntity.getMember(), memberBean);
			claimLineBean.setMember(memberBean);
		}

		if(claimLineEntity.getProvider() != null) {
			ProviderBean providerBean = new ProviderBean();
			BeanUtils.copyProperties(claimLineEntity.getProvider(), providerBean);
			claimLineBean.setProvider(providerBean);
		}
	}

}
