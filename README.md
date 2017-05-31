# neo_conflict
POC See if two sets of shortest paths intersect

1. Build it:

        mvn clean package

2. Copy target/conflict-1.0-SNAPSHOT.jar to the plugins/ directory of your Neo4j server.

3. (Re)Start Neo4j server.

4. Test it:

        CALL com.maxdemarzi.conflict(0,[6,8], 9, [4,2])
        CALL com.maxdemarzi.conflict(n1, nodesList1, n2, nodesList2)
        
        