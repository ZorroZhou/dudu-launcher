package com.wow.carlauncher.ex.plugin.fk.event;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PFkEventBatterLevel {
    private Integer level;
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public PFkEventBatterLevel setTotal(Integer total) {
        this.total = total;
        return this;
    }

    public Integer getLevel() {
        return level;
    }

    public PFkEventBatterLevel setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public PFkEventBatterLevel(Integer level, Integer total) {
        this.level = level;
        this.total = total;
    }

    public PFkEventBatterLevel() {
    }
}
