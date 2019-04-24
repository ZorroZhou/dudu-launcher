package com.wow.carlauncher.repertory.db.manage;

/**
 * Created by liuyixian on 15/11/26.
 */
public class BasePager {
    public static Integer MAX_PAGE_SIZE=10;

    private Integer page = 1;// 页数
    private Integer rows = MAX_PAGE_SIZE;// 每页显示多少条

    private Integer totalCount = 0;// 总条数
    private Integer pageCount = 1;// 总页数

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        if (page < 1) {
            page = 1;
        }
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if (rows < 1) {
            rows = 1;
        } else if(rows > MAX_PAGE_SIZE) {
            rows = MAX_PAGE_SIZE;
        }
        this.rows = rows;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        pageCount = totalCount / rows;
        if (totalCount % rows > 0) {
            pageCount ++;
        }
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}
