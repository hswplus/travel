package com.tarnett.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果封装对象
 * @param <T>
 */
public class PageResult<T> implements Serializable{//对象的序列化，（通过IO保存到磁盘）微服务项目必需要

	private static final long serialVersionUID = 1L;
    private Long totalCount;// 总记录数
    private Integer totalPage;// 总页数
    private Integer currentPage;// 当前页
    private Integer pageSize;// 页面记录数
    private List<T> dataList;// 当前页的记录

    public PageResult() {
    }

    public PageResult(Long totalCount, Integer totalPage, Integer currentPage, Integer pageSize, List<T> dataList) {
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.dataList = dataList;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
