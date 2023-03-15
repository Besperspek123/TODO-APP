package com.example.objectsDataBase;

import javax.persistence.*;

@Entity
@Table(name = "completedtasks")
public class CompletedTask {
    @Column(name = "task")
    private String task;

    @Column(name = "description")
    private String description;

    @Column(name = "user")
    private String user;

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public CompletedTask() {
    }

    public CompletedTask(String task, String description, String  user) {
        this.task = task;
        this.description = description;
        this.user = user;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String  user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
