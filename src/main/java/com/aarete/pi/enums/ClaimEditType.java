package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

public enum ClaimEditType {

    MULTI_LINE("Multi-Line"), SINGLE("Single");

    private static final Map<String, ClaimEditType> editTypeMap = new HashMap<>();
    private String name;

    ClaimEditType(String name) {
        this.name = name;
    }
    
    static {
        for (ClaimEditType value : values()) {
            editTypeMap.put(value.toString(), value);
        }
    }

    public static ClaimEditType getInstanceByName(String name) {
        return editTypeMap.get(name.toUpperCase());
    }

    @Override
    public String toString() {
        return this.name;
    }
}