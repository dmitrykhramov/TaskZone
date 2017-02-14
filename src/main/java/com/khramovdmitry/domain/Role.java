package com.khramovdmitry.domain;

import javax.persistence.*;

/**
 * Created by Dmitry on 13.02.2017.
 */
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

}
