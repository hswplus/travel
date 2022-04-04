package com.tarnett.controller;

import com.tarnett.entity.PageResult;
import com.tarnett.entity.Result;
import com.tarnett.mapper.UserMapper;
import com.tarnett.pojo.Favorite;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.RouteImg;
import com.tarnett.pojo.User;
import com.tarnett.service.FavoriteService;
import com.tarnett.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {
    @Autowired
    private RouteService routeService;
    @Autowired
    private FavoriteService favoriteService;
    @RequestMapping("/queryPage")
    public PageResult<Route> queryPage(Integer cid, Integer currentPage,String queryString) {
        // 1. 对获取的参数进行非空处理 cid为null ，说明没有cid，那么应该查询所有分类下面所有的产品
        if (cid == null) {
            cid = 0;
        }
        if (currentPage == null) {
            currentPage = 1;
        }
        // 2. 调用业务逻辑层的分页查询方法
        PageResult<Route> routePage = routeService.queryPage(cid, currentPage,queryString);
        return routePage;
    }

    @RequestMapping("/queryHot")
    public Result queryHot(@RequestParam(defaultValue = "5") Integer count) {
        // 1. 对数据进行非空处理 这里使用springMVC提供的注解来实现
        // 2. 调用业务逻辑层查询数据
        List<Route> routeList = routeService.queryHot(count);
        // 3. 响应数据
        return new Result(true, "", routeList);
    }

    @RequestMapping("/queryCount")
    public Result queryCount(@RequestParam(defaultValue = "8") Integer count) {
        List<Route> routeList = routeService.queryHot(count);
        return new Result(true, "", routeList);
    }

    @RequestMapping("/queryRouteAndSeller")
    public Result queryRouteAndSeller(Integer rid) {
        // 1. 调用业务逻辑层查询数据
        Route route = routeService.queryRouteAndSeller(rid);
        return new Result(true, "", route);
    }

    @RequestMapping("/queryRouteImages")
    public Result queryRouteImages(Integer rid) {
        List<RouteImg> routeImgList = routeService.queryRouteImages(rid);
        return new Result(true,"",routeImgList);
    }

    @RequestMapping("/queryMyFavorite")
    public Result queryMyFavorite(HttpSession session) {
        // 1.1 从session对象中获取登录的用户
        User user = (User) session.getAttribute("loginUser");
        // 1.2 判断用户是否登录
        if (user == null) {
            // 说明户没有登录
            return new Result(false, "");
        }
        // 2. 调用业务逻辑层查询数据
        List<Route> routeList = favoriteService.selectMyFavoriteByUid(user.getUid());
        System.out.println(routeList);
        return new Result(true, "", routeList);
    }


}
