package com.example.demo.entities;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class UserList {
    @Valid
    private List<User> users;

    public UserList() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

