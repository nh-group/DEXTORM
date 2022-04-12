package fr.pantheonsorbonne.cri.publisher.db.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

public class CoverageRunContext {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    long timestamp;

    public Collection<Requirement> getReq() {
        return req;
    }

    public void setReq(Collection<Requirement> req) {
        this.req = req;
    }

    @OneToMany(targetEntity = Requirement.class, mappedBy = "context")
    private Collection<Requirement> req;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
