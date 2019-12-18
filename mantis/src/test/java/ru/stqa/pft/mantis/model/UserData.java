package ru.stqa.pft.mantis.model;

import javax.persistence.*;

@Entity
@Table(name = "mantis_user_table")
public class UserData {

    @Id
    @Column (name = "id")
    private int id;

    @Column (name = "username")
    private String username;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserData withId(int id) {
        this.id = id;
        return this;
    }

    public UserData withUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}