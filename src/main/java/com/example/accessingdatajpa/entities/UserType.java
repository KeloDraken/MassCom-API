package com.example.accessingdatajpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "userTypes")
public class UserType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;
    private String userType;

    protected UserType() {
    }

    public UserType(User user, String userType) {
        this.user = user;
        this.userType = userType;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
