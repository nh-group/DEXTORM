package fr.pantheonsorbonne.cri.publisher.db.model;

import javax.persistence.*;

public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;


    long reqId;
    String reqURL;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getReqId() {
        return reqId;
    }

    public void setReqId(long reqId) {
        this.reqId = reqId;
    }

    public String getReqURL() {
        return reqURL;
    }

    public void setReqURL(String reqURL) {
        this.reqURL = reqURL;
    }


    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private CoverageRunContext context;

}
