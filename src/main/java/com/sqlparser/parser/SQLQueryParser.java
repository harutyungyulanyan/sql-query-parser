package com.sqlparser.parser;

import com.sqlparser.model.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SQLQueryParser {

    public Query parse(String sql) {
        Query query = new Query();

        // Regex patterns to match different parts of the query
        String selectPattern = "SELECT\\s+(.*?)\\s+FROM";
        String fromPattern = "FROM\\s+(.*?)\\s*(WHERE|GROUP BY|ORDER BY|LIMIT|$)";
        String joinPattern = "(LEFT|RIGHT|INNER|FULL)\\s+JOIN\\s+(.*?)\\s+ON\\s+(.*?)\\s*(WHERE|GROUP BY|ORDER BY|LIMIT|$)";
        String wherePattern = "WHERE\\s+(.*?)\\s*(GROUP BY|ORDER BY|LIMIT|$)";
        String groupByPattern = "GROUP BY\\s+(.*?)\\s*(HAVING|ORDER BY|LIMIT|$)";
        String orderByPattern = "ORDER BY\\s+(.*?)\\s*(LIMIT|$)";
        String limitPattern = "LIMIT\\s+(\\d+)";
        String offsetPattern = "OFFSET\\s+(\\d+)";

        // Parsing SELECT columns
        Matcher selectMatcher = Pattern.compile(selectPattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        if (selectMatcher.find()) {
            String columns = selectMatcher.group(1);
            // Split by commas and trim each column name to remove leading/trailing spaces
            query.setColumns(Arrays.stream(columns.split(","))
                    .map(String::trim)  // Trim each column name
                    .collect(Collectors.toList()));
        }

        // Parsing FROM sources
        Matcher fromMatcher = Pattern.compile(fromPattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        if (fromMatcher.find()) {
            String sources = fromMatcher.group(1);
            query.setFromSources(Arrays.asList(parseSources(sources)));
        }

        // Parsing JOIN clauses
        Matcher joinMatcher = Pattern.compile(joinPattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        while (joinMatcher.find()) {
            Join join = new Join(joinMatcher.group(1), joinMatcher.group(2), "", joinMatcher.group(3));
            query.getJoins().add(join);
        }

        // Parsing WHERE clauses
        Matcher whereMatcher = Pattern.compile(wherePattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        if (whereMatcher.find()) {
            query.getWhereClauses().add(new WhereClause(whereMatcher.group(1)));
        }

        // Parsing GROUP BY columns
        Matcher groupByMatcher = Pattern.compile(groupByPattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        if (groupByMatcher.find()) {
            query.setGroupByColumns(Arrays.asList(groupByMatcher.group(1).split(",")));
        }

        // Parsing ORDER BY columns
        Matcher orderByMatcher = Pattern.compile(orderByPattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        if (orderByMatcher.find()) {
            query.setSortColumns(Arrays.asList(parseSortColumns(orderByMatcher.group(1))));
        }

        // Parsing LIMIT and OFFSET
        Matcher limitMatcher = Pattern.compile(limitPattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        if (limitMatcher.find()) {
            query.setLimit(Integer.parseInt(limitMatcher.group(1)));
        }

        Matcher offsetMatcher = Pattern.compile(offsetPattern, Pattern.CASE_INSENSITIVE).matcher(sql);
        if (offsetMatcher.find()) {
            query.setOffset(Integer.parseInt(offsetMatcher.group(1)));
        }

        return query;
    }

    // Helper method to parse sources (tables) from the FROM clause
    private Source[] parseSources(String sources) {
        return Arrays.stream(sources.split(","))
                .map(source -> new Source(source.trim(), ""))  // Trim each table name
                .toArray(Source[]::new);
    }

    // Helper method to parse ORDER BY columns
    private Sort[] parseSortColumns(String columns) {
        return Arrays.stream(columns.split(","))
                .map(col -> {
                    String[] parts = col.trim().split(" ");
                    return new Sort(parts[0], parts.length > 1 ? parts[1] : "ASC");  // Default order is ASC
                })
                .toArray(Sort[]::new);
    }
}
