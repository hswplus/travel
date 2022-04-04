package com.tarnett.service.impl;

import com.tarnett.entity.Result;
import com.tarnett.mapper.FavoriteMapper;
import com.tarnett.mapper.RouteMapper;
import com.tarnett.pojo.Favorite;
import com.tarnett.pojo.Route;
import com.tarnett.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;
    @Autowired
    private RouteMapper routeMapper;

    @Override
    public Favorite queryFavorite(Integer rid, Integer uid) {
        // 1. 直接把查询的数据返回
        return favoriteMapper.selectByRidAndUid(rid,uid);
    }

    @Override
    public Favorite addFavorite(Integer rid, Integer uid) {
        // 1.往tab_favorite表中添加一条记录
        // 构建一个Favorite对象
        Favorite favorite = new Favorite();
        favorite.setRid(rid);
        favorite.setUid(uid);
        // 需要对日期进行格式化yyyy-MM-dd HH:mm:ss
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dataStr = format.format(new Date());
        favorite.setDate(dataStr);
        favoriteMapper.insert(favorite);
        // 2.更新tab_route表中该产品的收藏记录
        routeMapper.updateCountAddByRid(rid);
        return null;
    }

    @Override
    public void subFavorite(Integer rid, Integer uid) {
        // 1.往tab_favorite表中删除一条记录
        // 构建一个Favorite对象
        Favorite favorite = new Favorite();
        favorite.setRid(rid);
        favorite.setUid(uid);
        favoriteMapper.deleteByRidAndUid(favorite);
        // 2.更新tab_route表中该产品的收藏记录
        routeMapper.updateCountSubByRid(rid);

    }

    @Override
    public List<Route> selectMyFavoriteByUid(Integer uid) {
        // 1. 直接把查询的数据返回
        return favoriteMapper.selectMyFavoriteByUid(uid);
    }
}
