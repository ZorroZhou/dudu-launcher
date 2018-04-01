package com.wow.carlauncher.plugin;

import org.xutils.x;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 10124 on 2018/4/1.
 */

public class SimulateDoubleClickUtil<KEY> {
    private Map<KEY, Long> cache;
    private Map<KEY, Runnable> runner;


    public SimulateDoubleClickUtil() {
        cache = new ConcurrentHashMap<>();
        runner = new ConcurrentHashMap<>();
    }

    public void action(final KEY key, Runnable singleClick, Runnable doubleClick) {
        Long tt = cache.get(key);
        cache.put(key, System.currentTimeMillis());
        if (tt == null || System.currentTimeMillis() - tt > 500) {
            runner.put(key, singleClick);
            x.task().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runner.get(key).run();
                }
            }, 600);
        } else {
            runner.put(key, doubleClick);
        }
    }
}
