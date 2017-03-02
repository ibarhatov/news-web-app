package com.ibarhatov.newswebapp.model;

import javax.persistence.*;

/**
 * Created by Ivan on 25.02.2017.
 */
@Entity
@Table(name = "Category")
public class Category {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Integer id;

    @Column(name = "NAME")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
