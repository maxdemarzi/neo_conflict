package com.maxdemarzi;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.TestCase.assertEquals;
import static java.util.Collections.singletonMap;

public class ConflictTest {
    @Rule
    public final Neo4jRule neo4j = new Neo4jRule()
            .withFixture(MODEL_STATEMENT)
            .withProcedure(Conflict.class);

    @Test
    public void testConflict() throws Exception {
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY);
        String results = response.get("results").get(0).get("data").get(0).get("row").get(0).asText();
        assertEquals("Conflict", results);

        response = HTTP.POST(neo4j.httpURI().resolve("/db/data/transaction/commit").toString(), QUERY2);
        results = response.get("results").get(0).get("data").get(0).get("row").get(0).asText();
        assertEquals("No Conflict", results);

    }


    private static final Map QUERY =  singletonMap("statements", asList(singletonMap("statement",
            "CALL com.maxdemarzi.conflict(0,[6,8], 9, [4,2])")));

    private static final Map QUERY2 =  singletonMap("statements", asList(singletonMap("statement",
            "CALL com.maxdemarzi.conflict(0,[5,6], 9, [8,4])")));

    private static final String MODEL_STATEMENT =
            "CREATE (a0:MyLabel)" +
                    "CREATE (a1:MyLabel)" +
                    "CREATE (a2:MyLabel)" +
                    "CREATE (a3:MyLabel)" +
                    "CREATE (a4:MyLabel)" +
                    "CREATE (a5:MyLabel)" +
                    "CREATE (a6:MyLabel)" +
                    "CREATE (a7:MyLabel)" +
                    "CREATE (a8:MyLabel)" +
                    "CREATE (a9:MyLabel)" +
                    "CREATE (a0)-[:CONNECTED ]->(a1)" +
                    "CREATE (a1)-[:CONNECTED ]->(a2)" +
                    "CREATE (a2)-[:CONNECTED ]->(a3)" +
                    "CREATE (a2)-[:CONNECTED ]->(a4)" +
                    "CREATE (a2)-[:CONNECTED ]->(a5)" +
                    "CREATE (a3)-[:CONNECTED ]->(a6)" +
                    "CREATE (a6)-[:CONNECTED ]->(a7)" +
                    "CREATE (a4)-[:CONNECTED ]->(a8)" +
                    "CREATE (a8)-[:CONNECTED ]->(a9)";
}
