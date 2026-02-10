package com.ibosng.gatewayservice.services.impl;

import java.util.regex.Pattern;

public class JasperQueryValidator {


    private static final Pattern SAFE_QUERY_PATTERN = Pattern.compile(
            "(?i)^\\s*SELECT\\s+.*\\s+FROM\\s+.*"
    );

    private static final String[] BLOCKED_KEYWORDS = {"INSERT", "UPDATE", "DELETE", "DROP", "ALTER", "TRUNCATE"};
    private static final String[] RESTRICTED_TABLES = {"salaries", "user_credentials", "admin_logs"};

    public static boolean isQuerySafe(String query) {
        if (!SAFE_QUERY_PATTERN.matcher(query).matches()) {
            return false; // Only SELECT queries are allowed
        }

        for (String keyword : BLOCKED_KEYWORDS) {
            if (query.toUpperCase().contains(keyword)) {
                return false; // Prevents modification queries
            }
        }

        for (String table : RESTRICTED_TABLES) {
            if (query.toUpperCase().contains(table.toUpperCase())) {
                return false; // Prevents access to restricted tables
            }
        }

        return true;
    }
}
