package com.wow.frame.repertory.dbTool;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 10124 on 2017/7/26.
 */
public abstract class DatabaseInfo {
    public abstract String getDbPath();

    public abstract int getDbVersion();

    public abstract Class<?>[] getBeanClass();

    public boolean useUpgrade() {
        return false;
    }

    @SuppressWarnings("unused")
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
