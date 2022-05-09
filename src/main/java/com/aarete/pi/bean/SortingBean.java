package com.aarete.pi.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortingBean {

	@ApiModelProperty(notes = "Different set for Summary section and Claimline list", example = "Summary : USER_ACTION, CLAIM_NUM, CLAIM_LINE_NUM, OPP_AMT, EDIT, AARETE_EX_CODE, CLAIM_FROM_TYPE, BILL_TYPE_CODE, POS,\n" +
			"    CPT_CODE, REV_CODE, DRG_CODE, LOB, BILLED_AMT, ALLOWED_AMT, PAID_AMT, UNITS_PAID, STATE, PROV_NAME, PROV_TIN, PROV_NPI,\n" +
			"    PROV_SPECIALTY, CLAIM_START_DATE, CLAIM_END_DATE, RECEIVED_DATE, PAID_DATE, MEMBER_ID, PRIMARY_DX_CODE, BATCH_DATE, BATCH_NO,\n" +
			"    EDIT_TYPE, PRIORITIZATION_SCORE, ASSIGNER, OVERRIDE_REASON, PEND_REASON")
	private String sortBy;
	
	@ApiModelProperty(notes = "Defaulted to DESC", example = "DESC,ASC")
	private String sortType = "DESC";

}
