package com.wow.frame.repertory.dbTool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wow.carlauncher.common.util.DateUtil;
import com.wow.frame.repertory.dbTool.beanTool.BeanInfo;
import com.wow.frame.repertory.dbTool.beanTool.BeanManage;
import com.wow.frame.repertory.dbTool.beanTool.PropertyInfo;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked,unused")
public class DatabaseManage {
    private final static String TAG = "frame.DatabaseManage";

    public static synchronized void init(Context context, DatabaseInfo info) {
        if (!inited) {
            inited = true;
            DatabaseManage.context = context;
            DatabaseManage.dbHelper = new DatabaseHelper(DatabaseManage.context, info);
        }
    }

    private static boolean inited = false;

    //这是一个计次器
    private static AtomicInteger mOpenCounter = new AtomicInteger();
    // 主键
    private static final String KEY_NAME = "id";
    // 上下文
    private static Context context;
    // 数据库Helper
    private static DatabaseHelper dbHelper;
    // 数据库操作对象
    private static SQLiteDatabase sqLiteDatabase;

    private DatabaseManage() {
    }

    /**
     * 获取数据库管理器
     * <p/>
     * 上下文
     *
     * @return 数据库管理器
     */
    private synchronized static SQLiteDatabase openDatabase() {
        Log.e(TAG, "获取一个数据库管理器");
        if (!inited) {
            throw new IllegalStateException("请先初始化工具！！");
        }
        if (mOpenCounter.incrementAndGet() == 1) {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    /**
     * 关闭数据库
     */
    private synchronized static void closeDataBase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            sqLiteDatabase.close();
            dbHelper.close();
            sqLiteDatabase = null;
        }
    }

    /**
     * 向数据库中插入数据bean
     */
    public static <T extends BaseEntity> long insert(@NonNull BaseEntity entity) {
        Class<T> clazz = (Class<T>) entity.getClass();
        Table t = clazz.getAnnotation(Table.class);
        if (t == null) {
            return 0;
        }
        entity.setCreateDate(DateUtil.getNowTime());
        entity.setModifyDate(DateUtil.getNowTime());

        ContentValues cv = new ContentValues();
        BeanInfo info = BeanManage.self().getBeanInfo(clazz);
        PropertyInfo[] pis = info.getPropertyInfos();
        for (PropertyInfo pi : pis) {
            try {
                Object result = pi.getReadMethod().invoke(entity);
                if (result != null) {
                    if (result instanceof Boolean) {
                        if ((Boolean) result) {
                            cv.put(pi.getName(), 1);
                        } else {
                            cv.put(pi.getName(), 0);
                        }
                    } else {
                        cv.put(pi.getName(), result.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "数据库表插入：TableName:" + t.name() + " values:" + cv);
        openDatabase();
        long r = -1;
        try {
            long rowid = sqLiteDatabase.insert(t.name(), null, cv);
            Log.e(TAG, "数据库表插入：TableName:" + t.name() + " rowid:" + rowid);
            if (rowid > 0) {
                Cursor cursor = sqLiteDatabase.rawQuery("select id from " + t.name() + " where rowid = " + rowid, null);
                cursor.moveToFirst();
                r = cursor.getLong(0);
                entity.setId(r);
                cursor.close();
            } else {
                r = -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDataBase();
        return r;
    }

    public static <T extends BaseEntity> void insertSyn(@NonNull final BaseEntity entity) {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                synchronized (entity) {
                    insert(entity);
                }
            }
        });
    }

    /**
     * 更新一个bean,如果id为null,则失败
     *
     * @param entity 实体对象
     * @param where  修改条件，如"id=1"
     * @return boolean
     */
    public static <T extends BaseEntity> boolean update(@NonNull BaseEntity entity, @NonNull String where) {
        Class<T> clazz = (Class<T>) entity.getClass();
        Table t = clazz.getAnnotation(Table.class);
        if (t == null) {
            return false;
        }
        entity.setModifyDate(DateUtil.getNowTime());
        ContentValues cv = new ContentValues();
        BeanInfo info = BeanManage.self().getBeanInfo(clazz);
        PropertyInfo[] pis = info.getPropertyInfos();
        for (PropertyInfo pi : pis) {
            try {
                Object result = pi.getReadMethod().invoke(entity);

                if (result != null) {
                    if (!pi.getName().equals("id") && !pi.getName().equals("createDate")) {
                        if (result instanceof Boolean) {
                            if ((Boolean) result) {
                                cv.put(pi.getName(), 1);
                            } else {
                                cv.put(pi.getName(), 0);
                            }
                        } else {
                            cv.put(pi.getName(), result.toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        openDatabase();
        Log.e(TAG, "数据库表更新：TableName:" + t.name() + " values:" + cv + " where " + where);
        boolean r = false;
        try {
            r = sqLiteDatabase.update(t.name(), cv, where, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDataBase();
        return r;

    }

    public static <T extends BaseEntity> void updateSyn(@NonNull final BaseEntity entity, @NonNull final String where) {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                synchronized (entity) {
                    update(entity, where);
                }
            }
        });
    }

    /**
     * 通用删除一个bean
     *
     * @param where 删除条件，如"id=1"
     * @return boolean
     */
    public static <T extends BaseEntity> boolean delete(@NonNull Class<T> clazz, @NonNull String where) {
        Table t = clazz.getAnnotation(Table.class);
        if (t == null) {
            return false;
        }

        Log.e(TAG, "数据库表删除：TableName:" + t.name() + " where " + where);
        openDatabase();
        boolean re = false;
        try {
            int r = sqLiteDatabase.delete(t.name(), where, null);
            Log.e(TAG, "数据库表删除：TableName:" + t.name() + " 删除数量: " + r);
            re = r > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDataBase();
        return re;
    }

    /**
     * 查询所有
     *
     * @return List
     */
    public static <T> List<T> getList(@NonNull Class<T> clazz, String where) {
        // 查询
        BeanInfo info = BeanManage.self().getBeanInfo(clazz);
        if (info == null) {
            return null;
        }
        Table t = clazz.getAnnotation(Table.class);
        if (t == null) {
            Log.e(TAG, "Table: 导包错误");
            return null;
        }

        PropertyInfo[] pis = info.getPropertyInfos();
        String sql = "select * from " + t.name();
        if (!isNullOrEmpty(where)) {
            sql = sql + " where " + where;
        }
        List<T> list = new ArrayList<>();
        openDatabase();
        try {
            Log.e(TAG, "sql:" + sql);
            Cursor cur = sqLiteDatabase.rawQuery(sql, null);
            while (cur.moveToNext()) {
                Map<String, Object> map = new HashMap<>();
                String[] names = cur.getColumnNames();
                for (String n : names) {
                    map.put(n, cur.getString(cur.getColumnIndex(n)));
                }

                Object obj = clazz.newInstance();
                for (PropertyInfo pi : pis) {
                    String propertyName = pi.getName();
                    if (map.containsKey(propertyName)) {
                        Object value = map.get(propertyName);
                        pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
                    }
                }
                list.add((T) obj);
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDataBase();
        Log.e(TAG, "数据库表查询列表：getList:" + list);
        return list;
    }

    /**
     * 查询map对象
     *
     * @param sql 查询sql
     * @return map
     */
    public static Map<String, Object> getMap(@NonNull String sql) {
        openDatabase();
        try {
            Cursor cur = sqLiteDatabase.rawQuery(sql, null);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                Map<String, Object> map = new HashMap<>();
                String[] names = cur.getColumnNames();
                for (String n : names) {
                    map.put(n, cur.getString(cur.getColumnIndex(n)));
                }
                cur.close();
                return map;
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDataBase();
        return null;
    }

    /**
     * 查询条数
     *
     * @param sql 查询sql
     * @return int
     */
    public static int getCount(@NonNull String sql) {
        openDatabase();
        try {
            Cursor cur = sqLiteDatabase.rawQuery("select count(*) as count_tmp from (" + sql + ") ", null);
            if (cur.moveToFirst()) {
                int r = cur.getInt(0);
                cur.close();
                Log.e(TAG, "数据库查询条数： " + r);
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDataBase();
        return 0;
    }

    public static void doSql(@NonNull String sql) {
        openDatabase();
        try {
            sqLiteDatabase.rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDataBase();
    }

    public static <T> T getBean(@NonNull Class<T> clazz, @NonNull String where) {
        Table t = clazz.getAnnotation(Table.class);
        if (t == null) {
            return null;
        }
        if (!isNullOrEmpty(where)) {
            where = " where " + where;
        }
        String sql = "select * from " + t.name() + where;
        Log.e(TAG, sql);
        Map<String, Object> map = getMap(sql);
        if (map == null)
            return null;
        try {
            BeanInfo info = BeanManage.self().getBeanInfo(clazz);
            Object obj = clazz.newInstance();
            PropertyInfo[] pis = info.getPropertyInfos();
            for (PropertyInfo pi : pis) {
                String propertyName = pi.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    pi.getWriteMethod().invoke(obj, getValueByType(pi.getField().getType(), value));
                }
            }
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据限制条件查询该记录是否存在，存在则更新，不存在插入
     *
     * @param entity 实体
     * @param where  条件
     * @param <T>    实体的泛型
     */
    public static <T extends BaseEntity> void save(@NonNull T entity, @NonNull String where) {
        T t = (T) getBean(entity.getClass(), where);
        if (t != null) {
            entity.setId(t.getId());
            update(entity, "id=" + entity.getId());
        } else {
            insert(entity);
        }
    }

    public static <T extends BaseEntity> void save(@NonNull T entity) {
        boolean update = false;
        if (entity.getId() != null) {
            T t = (T) getBean(entity.getClass(), "id=" + entity.getId());
            if (t != null) {
                Log.d(TAG, "save-update:" + entity);
                update(entity, "id=" + entity.getId());
                update = true;
            }
        }
        if (!update) {
            Log.d(TAG, "save-insert:" + entity);
            insert(entity);
        }
    }

    public static <T extends BaseEntity> void saveSyn(final @NonNull T entity) {
        x.task().run(new Runnable() {
            @Override
            public void run() {
                synchronized (entity) {
                    save(entity);
                }
            }
        });
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private DatabaseInfo info;

        public DatabaseHelper(Context context, DatabaseInfo info) {
            super(context, info.getDbPath(), null, info.getDbVersion());
            this.info = info;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Class<?>[] clazzes = info.getBeanClass();
            for (Class<?> clazz : clazzes) {
                Table t = clazz.getAnnotation(Table.class);
                BeanInfo beanInfo = BeanManage.self().getBeanInfo(clazz);
                PropertyInfo[] pis = beanInfo.getPropertyInfos();
                Cursor tableInf = db.query("sqlite_master", new String[]{"sql"}, "type='table' and name='" + t.name() + "'", null, null, null, null);
                if (tableInf.getCount() > 0) {
                    tableInf.moveToFirst();
                    String csql = tableInf.getString(tableInf.getColumnIndex("sql"));
                    String[] tempRows = csql.substring(csql.indexOf("(") + 1, csql.indexOf(")")).replace("\n", "").replace("`", "").split(",");
                    for (PropertyInfo descriptor : pis) {
                        boolean have = false;
                        Class<?> pt = descriptor.getField().getClass();
                        if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
                            continue;
                        }

                        for (String row : tempRows) {
                            if (row.trim().lastIndexOf("\t") > -1) {
                                if (row.trim().split("\t")[0].trim().equals(descriptor.getName())) {
                                    have = true;
                                    break;
                                }
                            } else {
                                if (row.trim().split(" ")[0].trim().equals(descriptor.getName())) {
                                    have = true;
                                    break;
                                }
                            }
                        }
                        if (!have) {
                            String addsql = descriptor.getName() + " " + DatabaseManage.getDbType(pt);
                            db.execSQL("ALTER TABLE " + t.name() + " ADD COLUMN " + addsql);
                        }
                    }
                } else {
                    StringBuilder sqlString = new StringBuilder().append("CREATE TABLE IF NOT EXISTS ").append(t.name()).append(" (");
                    Log.e(TAG, sqlString.toString());
                    for (int i = 0; i < pis.length; i++) {
                        PropertyInfo pi = pis[i];
                        if (DatabaseManage.KEY_NAME.equals(pi.getName())) {
                            sqlString.append(DatabaseManage.KEY_NAME + " integer primary key AUTOINCREMENT");
                        } else {
                            sqlString.append(pi.getName()).append(" ").append(DatabaseManage.getDbType(pi.getField().getType()));
                        }
                        if (i == pis.length - 1) {
                            sqlString.append(")");
                        } else {
                            sqlString.append(",");
                        }
                    }
                    db.execSQL(sqlString.toString());
                }
                tableInf.close();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (info.useUpgrade()) {
                info.onUpgrade(db, oldVersion, newVersion);
            } else {
                onCreate(db);
            }
        }
    }

    private static String getDbType(Class<?> clazz) {
        if (clazz == null) {
            return "TEXT";
        }
        if (clazz.equals(Integer.class) || clazz.equals(int.class) || clazz.equals(Long.class) || clazz.equals(long.class) || clazz.equals(Short.class) || clazz.equals(short.class)) {
            return "INTEGER";
        } else if (clazz.equals(Double.class) || clazz.equals(double.class) || clazz.equals(Float.class) || clazz.equals(float.class)) {
            return "REAL";
        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return "INTEGER";
        } else {
            return "TEXT";
        }
    }

    private static Object getValueByType(Class<?> clazz, Object value) {
        if (clazz == null || value == null) {
            return null;
        }
        if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return Integer.parseInt(value + "") == 1;
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return Integer.parseInt(value + "");
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return Double.parseDouble(value + "");
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return Float.parseFloat(value + "");
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return Long.parseLong(value + "");
        } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return Short.parseShort(value + "");
        } else if (clazz.equals(String.class)) {
            return String.valueOf(value);
        } else {
            return null;
        }
    }

    private static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0; // string.isEmpty() in Java 6
    }
}
