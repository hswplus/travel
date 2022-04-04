package com.tarnett.controller;

import com.tarnett.entity.Result;
import com.tarnett.pojo.Favorite;
import com.tarnett.pojo.User;
import com.tarnett.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @RequestMapping("/queryFavorite")
    public Result queryFavorite(Integer rid , HttpSession session) {
        // 1. 判断用户是否登录
       // 1.1 从session对象中获取登录的用户
        User user = (User) session.getAttribute("loginUser");
        // 1.2 判断用户是否登录
        if (user == null) {
            // 说明户没有登录
            return new Result(false, "");
        }
        // 2. 调用业务逻辑层查询数据
        Favorite favorite = favoriteService.queryFavorite(rid, user.getUid());
        // 3. 响应数据
        return new Result(true, "", favorite);
    }

    @RequestMapping("/addFavorite")
    public Result addFavorite(Integer rid,String returnUrl,HttpSession session) {
        // 1. 判断用户是否登录
        // 1.1 从session对象中获取登录的用户
        User user = (User) session.getAttribute("loginUser");
        // 1.2 判断用户是否登录
        if (user == null) {
            // 说明户没有登录
            return new Result(false, "您尚未登录，请登录后再操作。<a href='login.html?returnUrl="+returnUrl+"'>立即登录</a>");
        }
        // 2. 调用业务逻辑层进行收藏操作
        try {
           favoriteService.addFavorite(rid, user.getUid());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "收藏失败，请稍后再操作");
        }
        return new Result(true,"收藏成功");
    }

    @RequestMapping("/subFavorite")
    public Result subFavorite(Integer rid,HttpSession session) {
        // 1. 判断用户是否登录
        // 1.1 从session对象中获取登录的用户
        User user = (User) session.getAttribute("loginUser");
        // 1.2 判断用户是否登录
        if (user == null) {
            // 说明户没有登录
            return new Result(false, "您尚未登录，请登录后再操作。");
        }
        // 2. 调用业务逻辑层进行取消收藏操作
        try {
            favoriteService.subFavorite(rid, user.getUid());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "取消收藏失败，请稍后再操作");
        }
        return new Result(true,"取消收藏成功");
    }

}
