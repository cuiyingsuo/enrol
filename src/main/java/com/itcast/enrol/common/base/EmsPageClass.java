package com.itcast.enrol.common.base;

import java.util.List;

/**
 * Created by liuzhongshuai on 2017/12/28.
 */
public class EmsPageClass {


    private Integer total;

    private Integer pageNo;

    public Integer getCountPerPage() {
        return countPerPage;
    }

    public void setCountPerPage(Integer countPerPage) {
        this.countPerPage = countPerPage;
    }

    private Integer countPerPage;

    private List<EmsClass> classes;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public List<EmsClass> getClasses() {
        return classes;
    }

    public void setClasses(List<EmsClass> classes) {
        this.classes = classes;
    }

}
