package org.example.model;

import javax.persistence.*;

@Entity
public class Property {
    @Id
    private Long id;
    private String name;

    Property() {}
    Property(Long id) {
        this.id = id;
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
}
