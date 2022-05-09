package com.aarete.pi.helper;

import static org.springframework.util.StringUtils.hasText;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.util.CollectionUtils;

import com.aarete.pi.bean.AssignClaimRequest;
import com.aarete.pi.bean.ClaimRequest;
import com.aarete.pi.bean.ClaimlineComment;
import com.aarete.pi.bean.MyFilterRequestBean;
import com.aarete.pi.bean.ClaimlineListDetailsRequest;
import com.aarete.pi.bean.User;
import com.aarete.pi.enums.Codes;
import com.aarete.pi.enums.SummaryBy;
import com.aarete.pi.enums.SummaryByColumns;
import com.aarete.pi.exception.BadRequestException;

public class ValidationHelper {

	private ValidationHelper() {
		
	}
	
	public static void validateSummaryByRequest(ClaimRequest claimRequest) throws BadRequestException {
		if (Objects.isNull(claimRequest) || Objects.isNull(claimRequest.getSummaryBy())
				|| !hasText(claimRequest.getSummaryBy().getSummaryBy())
				|| isNotInEnum(claimRequest.getSummaryBy().getSummaryBy(), SummaryBy.class)
				|| !hasText(claimRequest.getSummaryBy().getColumnOneName())
				|| isNotInEnum(claimRequest.getSummaryBy().getColumnOneName(), SummaryByColumns.class)
				|| !hasText(claimRequest.getSummaryBy().getColumnTwoName())
				|| isNotInEnum(claimRequest.getSummaryBy().getColumnOneName(), SummaryByColumns.class)) {
			throw new BadRequestException("SummaryBy section has wrong values.");
		}

		if (Objects.isNull(claimRequest.getEngagementFilters())
				|| !hasText(claimRequest.getEngagementFilters().getEngagementRole())) {
			throw new BadRequestException(
					"Engagement Role is Mandatory. Need values from AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER");
		}

	}

	private static boolean isNotInEnum(String value, Class enumClass) {
		return Arrays.stream(enumClass.getEnumConstants()).noneMatch(e -> e.toString().equals(value));
	}

	public static void validateAddComment(@Valid ClaimlineComment claimlineComment) throws BadRequestException {
		if (CollectionUtils.isEmpty(claimlineComment.getClaimLineIdList())) {
			throw new BadRequestException("No claimline is provided.");
		}
	}

	public static void validateGetClaimLineList(@Valid ClaimRequest claimRequest) throws BadRequestException {

	}

	public static void validateCodesList(MyFilterRequestBean myFilterRequestBean) throws BadRequestException {
		if (myFilterRequestBean == null || hasText(myFilterRequestBean.getFilterName())
				|| isNotInEnum(myFilterRequestBean.getFilterName().toUpperCase(), Codes.class)) {
			throw new BadRequestException("Validation failed");
		}

	}

	public static void validateAddUser(@Valid List<User> userList) throws BadRequestException {
		// TODO Auto-generated method stub
		if (CollectionUtils.isEmpty(userList)) {
			throw new BadRequestException("User details not provided.");
		}
	}
	
	public static void validateAssignClaimlines(@Valid AssignClaimRequest assignClaimRequest) throws BadRequestException {
		if (CollectionUtils.isEmpty(assignClaimRequest.getClaimProcessList())) {
			throw new BadRequestException("No claimline is provided.");
		}
	}
	
	public static void validateRequest(String param1, String param2) throws BadRequestException {
		// TODO Auto-generated method stub
		if (!hasText(param1) || !hasText(param2)) {
			throw new BadRequestException("Engagement details not provided");
		}
	}

    public static void validateClaimLineList(ClaimlineListDetailsRequest claimlineListDetailsRequest) throws BadRequestException{
		if (CollectionUtils.isEmpty(claimlineListDetailsRequest.getClaimLevelClaimLineIdList()) ||
				CollectionUtils.isEmpty(claimlineListDetailsRequest.getClaimlineLevelClaimLineIdList())) {
			throw new BadRequestException("No claimline is provided.");
		}
	}
}
