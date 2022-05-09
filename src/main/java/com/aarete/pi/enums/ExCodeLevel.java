package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

public enum ExCodeLevel {

    CLAIM, CLAIM_LINE;

    private static final Map<String, ExCodeLevel> exCodeLevelMap = new HashMap<>();

    static {
        for (ExCodeLevel value : values()) {
            exCodeLevelMap.put(value.toString(), value);
        }
    }

    public static ExCodeLevel getInstanceByName(String name) {
        return exCodeLevelMap.get(name.toUpperCase());
    }
}
