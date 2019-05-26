package com.wow.carlauncher.repertory.server.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageResponse<T> implements Serializable {
    private Long total = 0L;
    private List<T> rows = new ArrayList();

    public PageResponse() {
    }

    public Long getTotal() {
        return this.total;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public PageResponse<T> setTotal(Long total) {
        this.total = total;
        return this;
    }

    public PageResponse<T> setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public String toString() {
        return "PageResponse(total=" + this.getTotal() + ", rows=" + this.getRows() + ")";
    }
}