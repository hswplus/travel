package com.tarnett.service.impl;

import com.tarnett.mapper.CategoryMapper;
import com.tarnett.mapper.UserMapper;
import com.tarnett.pojo.Category;
import com.tarnett.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private JedisPool jedisPool;

    @Override
    public List<Category> queryAll() {
        // 1.查询redis缓存中是否有分类的数据
        Jedis jedis = jedisPool.getResource();
        Set<Tuple> category = jedis.zrangeWithScores("category", 0, -1);
        // 2.如果缓存中存在数据，则直接返回数据，不需要查询数据库
        List<Category> categoryList = new ArrayList<>();
        if (category != null && category.size() > 0) {
            // 说明缓存中存在数据，需要对数据类型进行转换
            for (Tuple tuple : category) {
                Category category1 = new Category();
                category1.setCid((int)tuple.getScore()); // 获取对象的分数(即cid)
                category1.setCname(tuple.getElement()); // 获取对象的值（即对象是name)
                categoryList.add(category1);
            }
            System.out.println("从redis缓存中获取数据");
            return categoryList;
        }
        // 3.如果缓存中没有数据，则调用数据访问层去数据库查询数据
        categoryList = categoryMapper.selectAll();
        // 4.将查询出来的结果保存到redis缓存中
        for (Category cate : categoryList) {
            jedis.zadd("category",cate.getCid(), cate.getCname());
        }
        // 5.返回数据
        System.out.println("从数据库中查询数据");
        return categoryList;


        /**
         * 下面是没有用redis 前的
         * */
        // 把数据访问层查询出来的结果直接返回给控制器即可，
//        System.out.println("从数据库中查询数据");
//        return  categoryMapper.selectAll();
        // 或使用
//        List<Category> categoryList= categoryMapper.selectAll();
//        return  categoryList;
    }
}
