package com.restaurant.utility;

import java.util.*;

/**
 * Lightweight JSON Serialization and Deserialization Helper.
 * Does not rely on any external library dependencies.
 */
public class JsonHelper {
    
    /**
     * Converts a Java Collection (Map/List/Array) or primitive/wrapper/string to JSON string.
     */
    public static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) {
            return "\"" + escapeJson((String) obj) + "\"";
        }
        if (obj instanceof Number || obj instanceof Boolean) {
            return obj.toString();
        }
        if (obj instanceof Map) {
            StringBuilder sb = new StringBuilder("{");
            Map<?, ?> map = (Map<?, ?>) obj;
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) sb.append(",");
                first = false;
                sb.append("\"").append(entry.getKey().toString()).append("\":").append(toJson(entry.getValue()));
            }
            sb.append("}");
            return sb.toString();
        }
        if (obj instanceof List) {
            StringBuilder sb = new StringBuilder("[");
            List<?> list = (List<?>) obj;
            boolean first = true;
            for (Object item : list) {
                if (!first) sb.append(",");
                first = false;
                sb.append(toJson(item));
            }
            sb.append("]");
            return sb.toString();
        }
        if (obj instanceof Object[]) {
            return toJson(Arrays.asList((Object[]) obj));
        }
        return "\"" + escapeJson(obj.toString()) + "\"";
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Regex-based parsing of JSON arrays of objects.
     * Generates a list of string key-value maps representing each object.
     */
    public static ArrayList<LinkedHashMap<String, String>> parseJsonArray(String json) {
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) return list;
        
        // Match contents inside {} brackets
        java.util.regex.Pattern objPattern = java.util.regex.Pattern.compile("\\{([^\\}]+)\\}");
        java.util.regex.Matcher objMatcher = objPattern.matcher(json);
        java.util.regex.Pattern kvPattern = java.util.regex.Pattern.compile("\"([^\"]+)\"\\s*:\\s*(?:\"([^\"]*)\"|([\\d\\.\\-\\w]+|true|false|null))");
        
        while (objMatcher.find()) {
            String objContent = objMatcher.group(1);
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            java.util.regex.Matcher kvMatcher = kvPattern.matcher(objContent);
            while (kvMatcher.find()) {
                String key = kvMatcher.group(1);
                String value = kvMatcher.group(2) != null ? kvMatcher.group(2) : kvMatcher.group(3);
                if ("null".equals(value)) {
                    value = null;
                }
                map.put(key, value);
            }
            if (!map.isEmpty()) {
                list.add(map);
            }
        }
        return list;
    }
}
