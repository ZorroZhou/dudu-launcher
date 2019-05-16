package com.wow.carlauncher.repertory.db.manage;

/**
 * Created by liuyixian on 15/11/26.
 */
public class PagerReq {
    public static Integer MAX_PAGE_SIZE = 10;

    private Integer page = 1;// 页数
    private Integer rows = MAX_PAGE_SIZE;// 每页显示多少条

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
        } else if (rows > MAX_PAGE_SIZE) {
            rows = MAX_PAGE_SIZE;
        }
        this.rows = rows;
    }
}
