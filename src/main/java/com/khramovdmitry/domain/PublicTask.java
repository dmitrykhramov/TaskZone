package com.khramovdmitry.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Dmitry on 12.02.2017.
 */

@Entity
public class PublicTask extends Task {

    @OneToOne
    private Response response;

    @OneToOne
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
