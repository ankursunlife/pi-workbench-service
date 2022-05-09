package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

public enum ClaimAction {
	ASSIGN, ACCEPT, REJECT, PEND, OVERRIDE;

    private static final Map<String, ClaimAction> claimActionMap = new HashMap<>();

    static {
        for (ClaimAction value : values()) {
        	claimActionMap.put(value.toString(), value);
        }
    }

    public static ClaimAction getInstanceByName(String name) {
        return claimActionMap.get(name.toUpperCase());
    }
}
