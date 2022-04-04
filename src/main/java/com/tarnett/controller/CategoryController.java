package com.tarnett.controller;

import com.tarnett.entity.Result;
import com.tarnett.pojo.Category;
import com.tarnett.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/queryAll")
    public Result queryAll() {
        // 需要调用业务逻辑层查询数据
        List<Category> categoryList = categoryService.queryAll();
        return  new Result(true,"",categoryList);
    }

}
