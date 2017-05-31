package com.maxdemarzi;

import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.impl.path.ShortestPath;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import org.neo4j.logging.Log;
import org.neo4j.procedure.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class Conflict {

    @Context
    public GraphDatabaseAPI db;

    @Context
    public Log log;

    private static final PathFinder<Path> finder = new ShortestPath( 9999, PathExpanders.allTypesAndDirections() );

    @Description("com.maxdemarzi.confict() ")
    @Procedure(name = "com.maxdemarzi.conflict", mode = Mode.WRITE)
    public Stream<StringResult> Conflict(@Name("n1") Long n1, @Name("nodesList1") List<Long> nodeList1,
                                         @Name("n2") Long n2, @Name("nodesList2") List<Long> nodeList2) {
        HashSet<Long> ids = new HashSet<>();

        Node node1 = db.getNodeById(n1);
        for (Long n : nodeList1) {
            Node node1a = db.getNodeById(n);
            Path p = finder.findSinglePath(node1, node1a);
            p.nodes().forEach(node -> ids.add(node.getId()));
        }
        Node node2 = db.getNodeById(n2);
        for (Long n : nodeList2) {
            Node node2a = db.getNodeById(n);
            Path p = finder.findSinglePath(node2, node2a);
            for (Node node : p.nodes()) {
                if (ids.contains(node.getId())) {
                    return Stream.of(new StringResult("Conflict"));
                }
            }
        }
        return Stream.of(new StringResult("No Conflict"));
    }
}
