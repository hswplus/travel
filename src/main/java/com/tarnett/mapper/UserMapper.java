package com.tarnett.mapper;

import com.tarnett.pojo.Category;
import com.tarnett.pojo.User;

import java.util.List;

public interface UserMapper {
    void insert(User user);

    User selectByUsername(String username);

    User selectByCode(String code);

    void updateStatus(User user);

    User selectByUsernameAndPassword(User user);

    List<Category> select();
}
