package com.tarnett.service.impl;

import com.tarnett.mapper.CategoryMapper;
import com.tarnett.mapper.UserMapper;
import com.tarnett.pojo.Category;
import com.tarnett.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryAll() {
        // 把数据访问层查询出来的结果直接返回给控制器即可，
        List<Category> categoryList= categoryMapper.selectAll();
//        return  categoryMapper.selectAll();
        return  categoryList;
    }
}
