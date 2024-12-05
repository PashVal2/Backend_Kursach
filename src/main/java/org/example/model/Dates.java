package org.example.model;

import javax.persistence.*;

@Entity
public class Dates { // даты
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "date_id")
    private Long id;
    private int year;
    private int month;
    private int day;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
    public Dates() {}
    public Dates(Long id, int year, int month, int day, User user, Property property) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.user = user;
        this.property = property;
    }
    public void setDate(BookingDates dates) {
        this.month = dates.getMonth();
        this.day = dates.getDay();
        this.year = dates.getYear();
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
