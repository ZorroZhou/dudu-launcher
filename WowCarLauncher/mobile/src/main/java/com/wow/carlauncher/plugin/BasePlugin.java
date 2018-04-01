package com.wow.carlauncher.plugin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wow.carlauncher.plugin.amapcar.AMapCarPluginListener;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 10124 on 2017/10/26.
 */

public class BasePlugin<L> {
    protected Context context;

    public Context getContext() {
        return context;
    }

    public BasePlugin() {
    }

    public void init(Context context) {
        this.context = context;
    }

    private List<L> listeners = Collections.synchronizedList(new ArrayList<L>());

    public void addListener(L l) {
        listeners.add(l);
    }

    public void removeListener(L l) {
        listeners.remove(l);
    }

    public void runListener(final ListenerRuner<L> runer) {
        x.task().autoPost(new Runnable() {
            @Override
            public void run() {
                for (L l : listeners) {
                    try {
                        runer.run(l);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface ListenerRuner<L> {
        void run(L l);
    }
}
