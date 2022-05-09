package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

public enum ClaimStatus {
    GROUP_QUEUE, MY_QUEUE, CLOSED, PEND, WAITING;

    private static final Map<String, ClaimStatus> claimStatusMap = new HashMap<>();

    static {
        for (ClaimStatus value : values()) {
            claimStatusMap.put(value.toString(), value);
        }
    }


    public static ClaimStatus getInstanceByName(String name) {
        return claimStatusMap.get(name.toUpperCase());
    }
}
