package org.example.model;

import javax.persistence.*;

@Entity
public class Role { // роль у юзера
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'USER' NOT NULL")
    private String name;
    public Role() {}
    public Role(Long id, String name) {
        this.name = name;
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
