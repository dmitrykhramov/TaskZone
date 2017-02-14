package com.khramovdmitry.domain;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Dmitry on 13.02.2017.
 */

@Entity
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String comment;
    private byte[] document;
    private LocalDate date;

    @OneToOne
    private PublicTask task;

    public Response() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public PublicTask getTask() {
        return task;
    }

    public void setTask(PublicTask task) {
        this.task = task;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
