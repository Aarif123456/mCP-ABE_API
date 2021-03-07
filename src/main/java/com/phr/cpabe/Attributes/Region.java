package com.phr.cpabe.Attributes;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Region {
    @Id
    @GeneratedValue
    private Long id;
    String name;

    public Region() {
        // Empty constructor required as of Neo4j API 2.0.5
    }

    public Region(String name) {
        this.name = name;
    }

    /**
     * Neo4j doesn't REALLY have bi-directional relationships. It just means when querying
     * to ignore the direction of the relationship.
     * https://dzone.com/articles/modelling-data-neo4j
     */
    @Relationship(type = "contains", direction = Relationship.UNDIRECTED)
    public Set<Organization>  organizations;

    public void addOrganization(Organization org) {
        if (organizations == null) {
            organizations = new HashSet<>();
        }
        organizations.add(org);
    }
}
