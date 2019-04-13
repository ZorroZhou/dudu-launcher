package com.wow.carlauncher.ex.plugin.amapcar.event;

import com.wow.carlauncher.ex.plugin.amapcar.model.Lukuang;

/**
 * Created by 10124 on 2018/4/22.
 */

public class PAmapLukuangInfo {
    private Lukuang lukuang;

    public Lukuang getLukuang() {
        return lukuang;
    }

    public PAmapLukuangInfo setLukuang(Lukuang lukuang) {
        this.lukuang = lukuang;
        return this;
    }
}
