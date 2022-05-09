package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

public enum ClaimLineSort {

    USER_ACTION, CLAIM_NUM, CLAIM_LINE_NUM, OPP_AMT, EDIT, AARETE_EX_CODE, CLAIM_FROM_TYPE, BILL_TYPE_CODE, POS,
    CPT_CODE, REV_CODE, DRG_CODE, LOB, BILLED_AMT, ALLOWED_AMT, PAID_AMT, UNITS_PAID, STATE, PROV_NAME, PROV_TIN, PROV_NPI,
    PROV_SPECIALTY, CLAIM_START_DATE, CLAIM_END_DATE, RECEIVED_DATE, PAID_DATE, MEMBER_ID, PRIMARY_DX_CODE, BATCH_DATE, BATCH_NO,
    EDIT_TYPE, PRIORITIZATION_SCORE, ASSIGNER, OVERRIDE_REASON, PEND_REASON, CPT_DESC, REVENUE_DESC, DIAGNOSIS1_DESC,
    MULTI_LINE_EDIT, MODIFIER, P_SCORE, AARETE_ASSIGNED_USER, CLIENT_ASSIGNED_USER;

    private static final Map<String, ClaimLineSort> claimLineMap = new HashMap<>();

    static {
        for (ClaimLineSort value : values()) {
            claimLineMap.put(value.toString(), value);
        }
    }

    public static ClaimLineSort getInstanceByName(String name) {
    	if(!StringUtils.hasText(name)) {
    		name = PROV_NAME.name();
        }
        return claimLineMap.get(name.toUpperCase());
    }
}
