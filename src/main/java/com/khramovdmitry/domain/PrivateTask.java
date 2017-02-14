package com.khramovdmitry.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Dmitry on 13.02.2017.
 */

@Entity
public class PrivateTask extends Task {

    @ManyToOne
    private User user;

    public PrivateTask() {
    }

    public PrivateTask(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
