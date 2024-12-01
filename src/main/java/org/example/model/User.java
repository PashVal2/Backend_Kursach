package org.example.model;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 255)
    private String password;
    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    // Конструкторы, геттеры и сеттеры
    public User() {}
    public User(Long id, String username, String password) {
        this.id = id;
        this.name = username;
        this.password = password;
    }
    // Геттеры и сеттеры
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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
