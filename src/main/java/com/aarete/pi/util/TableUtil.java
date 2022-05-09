package com.aarete.pi.util;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class TableUtil {

    private static final Map<String, List<String>> tableColumns;

    static {
        tableColumns = new HashMap<>();
        tableColumns.put("state", new LinkedList<>(List.of("state_id", "state_name", "state_desc")));
        tableColumns.put("lob", new LinkedList<>(List.of("lob_id", "lob_name", "lob_desc")));
        tableColumns.put("role", new LinkedList<>(List.of("role_id", "role_name", "role_desc")));
    }

    private TableUtil() {
    }

    public static List<String> getColumns(String tableName) {
        return tableColumns.get(tableName);
    }

    public static String getColumnsStr(String tableName) {
        return tableColumns.get(tableName).stream().collect(Collectors.joining(","));
    }

    public static String getColumnsStrExceptId(String tableName) {
        return tableColumns.get(tableName).stream().skip(1).collect(Collectors.joining(","));
    }
}
