package com.wow.carlauncher.repertory.db;

import com.wow.carlauncher.repertory.db.model.Trip;
import com.wow.carlauncher.repertory.db.model.TripPoint;
import com.wow.frame.repertory.dbTool.DatabaseInfo;

/**
 * Created by 10124 on 2018/5/12.
 */

public class AppDbInfo extends DatabaseInfo {
    @Override
    public String getDbPath() {
        return "wow_car_launcher";
    }

    @Override
    public int getDbVersion() {
        return 2;
    }

    @Override
    public Class<?>[] getBeanClass() {
        return new Class[]{Trip.class, TripPoint.class};
    }
}
