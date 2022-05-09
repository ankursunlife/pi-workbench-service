package com.aarete.pi.enums;

import java.util.HashMap;
import java.util.Map;

public enum Role {
    AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER;

    private static final Map<String, Role> roleMap = new HashMap<>();

    static {
        for (Role value : values()) {
            roleMap.put(value.toString(), value);
        }
    }

    public static Role getInstanceByName(String name) {
        return roleMap.get(name.toUpperCase());
    }
}
