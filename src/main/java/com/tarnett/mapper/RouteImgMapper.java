package com.tarnett.mapper;

import com.tarnett.pojo.RouteImg;

import java.util.List;

public interface RouteImgMapper {
    List<RouteImg> selectByRid(Integer rid);
}
