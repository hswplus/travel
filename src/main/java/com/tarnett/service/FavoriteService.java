package com.tarnett.service;

import com.tarnett.pojo.Favorite;
import com.tarnett.pojo.Route;

import java.util.List;

public interface FavoriteService {
    Favorite queryFavorite(Integer rid, Integer uid);

    Favorite addFavorite(Integer rid, Integer uid);

    void subFavorite(Integer rid, Integer uid);


    List<Route> selectMyFavoriteByUid(Integer uid);
}
