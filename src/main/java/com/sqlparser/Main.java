package com.sqlparser;

import com.sqlparser.model.Query;
import com.sqlparser.parser.SQLQueryParser;

public class Main {
    public static void main(String[] args) {
        String sql = "SELECT author.name, count(book.id), sum(book.cost) " +
                     "FROM author " +
                     "LEFT JOIN book ON (author.id = book.author_id) " +
                     "WHERE author.age > 30 " +
                     "GROUP BY author.name " +
                     "ORDER BY author.name ASC " +
                     "LIMIT 10 OFFSET 5";

        SQLQueryParser parser = new SQLQueryParser();
        Query query = parser.parse(sql);

        // Output the parsed query structure
        System.out.println(query);
    }
}
