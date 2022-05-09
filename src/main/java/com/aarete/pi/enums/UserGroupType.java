package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserGroupType {
    AU, LVL1, LVL2, LVL3;

    private static final Map<String, UserGroupType> roleMap = new HashMap<>();

    static {
        for (UserGroupType value : values()) {
            roleMap.put(value.toString(), value);
        }
    }
    public static UserGroupType getInstanceByName(String name) {
        return roleMap.get(name.toUpperCase());
    }
}
