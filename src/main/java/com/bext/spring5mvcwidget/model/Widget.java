package com.bext.spring5mvcwidget.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Widget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Integer version = 0;

    public Widget() {
    }

    public Widget(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Widget(String name, String description, Integer version) {
        this.name = name;
        this.description = description;
        this.version = version;
    }

    public Widget(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Widget(Long id, String name, String description, Integer version) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Widget{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version=" + version +
                '}';
    }
}
