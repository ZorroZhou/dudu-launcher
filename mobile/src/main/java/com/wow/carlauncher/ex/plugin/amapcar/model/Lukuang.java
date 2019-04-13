package com.wow.carlauncher.ex.plugin.amapcar.model;

import java.util.List;

public class Lukuang {
    private boolean tmc_segment_enabled; //路况柱状图是否显示 true/false 为false则不显示信息
    private int tmc_segment_size;//路况柱状图分成几段
    private int total_distance;// 总路程，单位米
    private int residual_distance;//剩余总路程，单位米
    private int finish_distance;//行驶过的里程，单位米
    private List<TmcInfo> tmc_info;//柱状图信息

    public boolean isTmc_segment_enabled() {
        return tmc_segment_enabled;
    }

    public Lukuang setTmc_segment_enabled(boolean tmc_segment_enabled) {
        this.tmc_segment_enabled = tmc_segment_enabled;
        return this;
    }

    public int getTmc_segment_size() {
        return tmc_segment_size;
    }

    public Lukuang setTmc_segment_size(int tmc_segment_size) {
        this.tmc_segment_size = tmc_segment_size;
        return this;
    }

    public int getTotal_distance() {
        return total_distance;
    }

    public Lukuang setTotal_distance(int total_distance) {
        this.total_distance = total_distance;
        return this;
    }

    public int getResidual_distance() {
        return residual_distance;
    }

    public Lukuang setResidual_distance(int residual_distance) {
        this.residual_distance = residual_distance;
        return this;
    }

    public int getFinish_distance() {
        return finish_distance;
    }

    public Lukuang setFinish_distance(int finish_distance) {
        this.finish_distance = finish_distance;
        return this;
    }

    public List<TmcInfo> getTmc_info() {
        return tmc_info;
    }

    public Lukuang setTmc_info(List<TmcInfo> tmc_info) {
        this.tmc_info = tmc_info;
        return this;
    }

    public static class TmcInfo {
        private int tmc_status;//每段柱状图信息 -1 无效, 0 无交通流(蓝色), 1 畅通（绿色）, 2 缓行（黄色）, 3 拥堵（红色）, 4 严重拥堵（深红色）, 10 行驶过的路段（灰色）
        private int tmc_segment_number;//路况柱状图每段的编号，编号越小越靠近起点
        private int tmc_segment_distance;// 路况柱状图每段的路程距离，单位米，所有段加起来的距离等于剩余总路程距离（每段柱状图的百分比为tmc_segment_distance除以residual_distance的值）
        private int tmc_segment_percent;//每段路况柱状图占总路程的百分比

        public int getTmc_status() {
            return tmc_status;
        }

        public TmcInfo setTmc_status(int tmc_status) {
            this.tmc_status = tmc_status;
            return this;
        }

        public int getTmc_segment_number() {
            return tmc_segment_number;
        }

        public TmcInfo setTmc_segment_number(int tmc_segment_number) {
            this.tmc_segment_number = tmc_segment_number;
            return this;
        }

        public int getTmc_segment_distance() {
            return tmc_segment_distance;
        }

        public TmcInfo setTmc_segment_distance(int tmc_segment_distance) {
            this.tmc_segment_distance = tmc_segment_distance;
            return this;
        }

        public int getTmc_segment_percent() {
            return tmc_segment_percent;
        }

        public TmcInfo setTmc_segment_percent(int tmc_segment_percent) {
            this.tmc_segment_percent = tmc_segment_percent;
            return this;
        }
    }
//
//    {
//        " tmc_segment_enabled ": true,
//            " tmc_segment_size": 2,
//            " total_distance ": 400,
//            "residual_distance": 212,
//            "finish_distance": 188,
//            "tmc_info": [
//        {
//            "tmc_segment_number": "1",
//                "tmc_status": "0",
//                "tmc_segment_distance": "100",
//                "tmc_segment_percent": "20",
//        },
//        {
//            "tmc_segment_number": "2",
//                "tmc_status": "2",
//                " tmc_segment_percent ": "80",
//        }
//]
//    }
}
