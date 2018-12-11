package com.webkettle.sql.entity.result;

import java.util.List;

/**
 * 分页实体封装
 * @author gsk
 * @param <T>
 *     列表实体
 */
public class PageResult<T> {

    private int page;

    private int pageSize;

    private long total;

    private long totalPage;

    private List<T> list;

    public static<T> PageResult<T> init(int page, int pageSize, long total, List<T> list){
        PageResult<T> result = new PageResult<>();
        result.setList(list);
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setTotalPage(total / pageSize + (total % pageSize > 0 ? 1 : 0));
        return result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
