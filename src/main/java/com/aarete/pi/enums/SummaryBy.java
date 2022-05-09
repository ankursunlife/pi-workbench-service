package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

public enum SummaryBy {

    EDIT, EX_CODE, PROVIDER_NAME, PROVIDER_NPI, PROVIDER_TIN, CPT_CODE, REV_CODE, BILL_TYPE, POS, BATCH,
    DATE_OF_SERVICE, CLAIM_PAID_DATE, CLAIM_RECEIVE_DATE;

    private static final Map<String, SummaryBy> summaryByMap = new HashMap<>();

    static {
        for (SummaryBy value : values()) {
            summaryByMap.put(value.toString(), value);
        }
    }

    public static SummaryBy getInstanceByName(String name) {
    	if(!StringUtils.hasText(name)) {
    		name = PROVIDER_NAME.name();
        }
        return summaryByMap.get(name.toUpperCase());
    }
}
