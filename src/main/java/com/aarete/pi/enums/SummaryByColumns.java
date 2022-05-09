package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

public enum SummaryByColumns {

    CLAIMS, CLAIM_LINES, UNITS, PAID, OPPORTUNITY, BILLED, NAME;

    private static final Map<String, SummaryByColumns> summaryByColumnsMap = new HashMap<>();

    static {
        for (SummaryByColumns value : values()) {
        	summaryByColumnsMap.put(value.toString(), value);
        }
    }

    public static SummaryByColumns getInstanceByName(String name) {
    	if(!hasText(name)) {
    		name = OPPORTUNITY.name();
        }
        return summaryByColumnsMap.get(name.toUpperCase());
    }
}
