package com.tarnett.service;

import com.tarnett.entity.PageResult;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.RouteImg;

import java.util.List;

public interface RouteService {
    PageResult<Route> queryPage(Integer cid, Integer currentPage,String queryString);

    List<Route> queryHot(Integer count);

    Route queryRouteAndSeller(Integer rid);


    List<RouteImg> queryRouteImages(Integer rid);

}
