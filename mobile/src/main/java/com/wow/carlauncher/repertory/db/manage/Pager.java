package com.wow.carlauncher.repertory.db.manage;

import java.util.List;
import java.util.Map;

public class Pager extends PagerReq {
    private String selectSql;
    private String[] param;

	private String sort = "id";// 排序
	private String order = "asc";// 排序方式 desc
	private List<Map<String, Object>> list;// 数据List

	public Pager (Integer page,Integer rows){
		setPage(page);
		setRows(rows);
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public int getStartItem(){
		return (getPage()-1)*getRows();
	}

	public int getEndItem(){
		return getPage()*getRows();
	}

    public String getSelectSql() {
        return selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public String[] getParam() {
        return param;
    }

    public void setParam(String[] param) {
        this.param = param;
    }
}
