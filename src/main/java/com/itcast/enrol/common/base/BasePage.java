package com.itcast.enrol.common.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuzs
 *         Created by liuzhongshuai on 2017/10/22.
 *         enrol 应用自定义分页类
 */
public class BasePage<T> implements Serializable {

    private int totalPage;

    private int currentPage;

    private int pageSize;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    private Long count;

    private List<T> pageData;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }
}
