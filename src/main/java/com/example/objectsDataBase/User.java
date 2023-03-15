package com.example.objectsDataBase;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Column(name = "loginUsername")
    private String loginUsername;

    @Column(name = "loginPassword")
    private String loginPassword;

    @Column(name = "username")
    private String username;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public User(String loginUsername, String loginPassword) {

        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;

    }

    public User() {
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
