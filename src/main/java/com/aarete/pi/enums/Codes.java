package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

public enum Codes {

    CPT, DX, REV, EX_CODE;

    private static final Map<String, Codes> filterCodesMap = new HashMap<>();

    static {
        for (Codes value : values()) {
            filterCodesMap.put(value.toString(), value);
        }
    }

    public static Codes getInstanceByName(String name) {
        return filterCodesMap.get(name.toUpperCase());
    }
}
