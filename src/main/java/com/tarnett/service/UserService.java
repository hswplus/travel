package com.tarnett.service;

import com.tarnett.pojo.User;

public interface UserService {

    void registe(User user);

    User checkUsernameExsit(String username);

    public User active(String code);

    User login(User user);
}
