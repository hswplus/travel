package com.tarnett.mapper;

import com.tarnett.pojo.Favorite;
import com.tarnett.pojo.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteMapper {
    Favorite selectByRidAndUid(@Param("rid") Integer rid, @Param("uid") Integer uid);


    void insert(Favorite favorite);

    void deleteByRidAndUid(Favorite favorite);

    List<Route> selectMyFavoriteByUid(Integer uid);
}
