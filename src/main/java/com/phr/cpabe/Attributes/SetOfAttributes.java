//package com.phr.cpabe.Attributes;
//
//import org.neo4j.ogm.annotation.GeneratedValue;
//import org.neo4j.ogm.annotation.Id;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Relationship;
//
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//// Class used to get the set of attributes from Neo4j
//// It will be called by the trusted authority when making the public and master-key and it will also be called by
//// Data owners when searching for attributes of entities they want to allow access to their file
//@NodeEntity
//public class SetOfAttributes {
//    @Id
//    @GeneratedValue
//    private Long id;
//
//    private String[] attributes;
//
//    private SetOfAttributes() {
//        // Empty constructor required as of Neo4j API 2.0.5
//    };
//
//    public SetOfAttributes(String name) {
//        this.name = name;
//    }
//
//    /**
//     * Neo4j doesn't REALLY have bi-directional relationships. It just means when querying
//     * to ignore the direction of the relationship.
//     * https://dzone.com/articles/modelling-data-neo4j
//     */
//    @Relationship(type = "TEAMMATE", direction = Relationship.UNDIRECTED)
//    public Set<SetOfAttributes> teammates;
//
//    public void worksWith(SetOfAttributes person) {
//        if (teammates == null) {
//            teammates = new HashSet<>();
//        }
//        teammates.add(person);
//    }
//
//    public String toString() {
//
//        return this.name + "'s teammates => "
//                + Optional.ofNullable(this.teammates).orElse(
//                Collections.emptySet()).stream()
//                .map(SetOfAttributes::getName)
//                .collect(Collectors.toList());
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}
