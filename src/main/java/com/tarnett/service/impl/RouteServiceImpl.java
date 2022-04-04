package com.tarnett.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tarnett.entity.PageResult;
import com.tarnett.mapper.RouteImgMapper;
import com.tarnett.mapper.RouteMapper;
import com.tarnett.pojo.Route;
import com.tarnett.pojo.RouteImg;
import com.tarnett.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;
    @Autowired
    private RouteImgMapper routeImgMapper;

    @Override
    public PageResult<Route> queryPage(Integer cid, Integer currentPage,String queryString) {
        // 1. 分页使用的是Mybatis的分页插件，pageHelper
        // 2. 开启分页
        int pageSize = 10;
        /**
         * 开启分页方法需要两个参数：
         * pageNum:当前页码 及时currentPage
         * pageSize: 每页记录数 pageSize
         */
        PageHelper.startPage(currentPage, pageSize);
        // 调用数据访问层查询数据
        // 组装模糊查询的参数
        queryString = "%" + queryString +"%";
        Page<Route> pages = routeMapper.selectPageByCid(cid,queryString);
        // 4. 构建返回对象
        return new PageResult<Route>(pages.getTotal(),pages.getPages(),currentPage,pageSize,pages.getResult());
    }

    @Override
    public List<Route> queryHot(Integer count) {
        return routeMapper.selectByCountDescLimit(count);
    }

    @Override
    public Route queryRouteAndSeller(Integer rid) {
        return routeMapper.selectRouteAndSellerByRid(rid);
    }

    @Override
    public List<RouteImg> queryRouteImages(Integer rid) {
        return routeImgMapper.selectByRid(rid);
    }

}
