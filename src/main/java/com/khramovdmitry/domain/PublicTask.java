package com.khramovdmitry.domain;


import javax.persistence.*;

/**
 * Created by Dmitry on 12.02.2017.
 */

@Entity
public class PublicTask extends Task {

    @OneToOne(cascade = CascadeType.ALL)
    private Response response;

    @ManyToOne
    private User assignee;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }
}
