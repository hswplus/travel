package com.tarnett.service.impl;

import com.tarnett.mapper.UserMapper;
import com.tarnett.pojo.User;
import com.tarnett.service.UserService;
import com.tarnett.util.MailUtils;
import com.tarnett.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


// 申明该类是一个业务逻辑层的实现类
@Service
public class UserServiceImpl implements UserService {
    // 自动注入
    @Autowired
    private UserMapper userMapper;

    @Override
    public void registe(User user) {
        // 1. 校验用户名的唯一性 (校验是否已经被注册），必须做，异步请求返回来后？ TODO
        User checkUser = userMapper.selectByUsername(user.getUsername());
        if (checkUser != null) {
            throw new RuntimeException();
        }

        // 2. 需要对密码进行加密操作
        try {
            String newPassword =  Md5Util.encodeByMd5(user.getPassword());
            user.setPassword(newPassword);// 将加密后的密码设置到user对象里面去
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 在保存用户之前需要涩会给你成一个UUID激活码，并且设置到用户里面
        String code = UUID.randomUUID().toString();
        user.setCode(code);
        // 设置用户激活 的状态，初始的情况下，用户的激活状态为N
        user.setStatus("N");

        // 3. 调用数据访问层的方法保存数据
        userMapper.insert(user);

        // 4. 给用户发送激活邮件
        String content = "恭喜您注册成功，点击立即激活，激活账号。<a href='http://localhost:8080/user/active.do?code="+code+"'>【立即激活】</a>";
        System.out.println(content);
        MailUtils.sendMail(user.getEmail(), content, "【融云旅游网】激活邮件");
    }

    @Override
    public User checkUsernameExsit(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User active(String code) {
        // 1. 根据激活码查询用户
        User user = userMapper.selectByCode(code);
        if (user == null) {
            return null;
        }
        // 如果用户不为空，而且用户的状态是未激活，才需要去修改用户的状态
        if (user.getStatus().equals("N")) {
            userMapper.updateStatus(user);
        }
        return user;
    }

    @Override
    public User login(User user) {
        // 1. 需要对密码进行加密
        try {
            String newPassword =  Md5Util.encodeByMd5(user.getPassword());
            user.setPassword(newPassword);// 将加密后的密码设置到user对象里面去
        } catch (Exception e) {
            e.printStackTrace();
        }
        User loginUser = userMapper.selectByUsernameAndPassword(user);
        return loginUser;
    }

}
