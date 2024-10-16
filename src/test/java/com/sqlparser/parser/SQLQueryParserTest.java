package com.sqlparser.parser;

import com.sqlparser.model.Query;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SQLQueryParserTest {

    @Test
    public void testSimpleQuery() {
        String sql = "SELECT * FROM book";
        SQLQueryParser parser = new SQLQueryParser();
        Query query = parser.parse(sql);

        assertNotNull(query);
        assertEquals(1, query.getFromSources().size());
    }

    @Test
    public void testComplexQuery() {
        String sql = "SELECT author.name, count(book.id), sum(book.cost) " +
                "FROM author " +
                "LEFT JOIN book ON (author.id = book.author_id) " +
                "WHERE author.age > 30 " +
                "GROUP BY author.name " +
                "ORDER BY author.name ASC " +
                "LIMIT 10 OFFSET 5";

        SQLQueryParser parser = new SQLQueryParser();
        Query query = parser.parse(sql);

        assertNotNull(query);
        assertEquals(3, query.getColumns().size());
        assertEquals(1, query.getJoins().size());
        assertEquals(1, query.getWhereClauses().size());
        assertEquals(10, query.getLimit());
    }
}
