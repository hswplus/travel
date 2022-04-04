package com.tarnett.mapper;

import com.github.pagehelper.Page;
import com.tarnett.pojo.Route;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteMapper {
    Page<Route> selectPageByCid(@Param("cid") Integer cid,@Param("queryString") String queryString);

    List<Route> selectByCountDescLimit(Integer count);

    Route selectRouteAndSellerByRid(Integer rid);

    void updateCountAddByRid(Integer rid);

    void updateCountSubByRid(Integer rid);
}
