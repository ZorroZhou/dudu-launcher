package com.wow.frame.declare;

import com.wow.frame.repertory.dbTool.DatabaseInfo;

/**
 * Created by 10124 on 2017/7/26.
 * 数据库的声明接口，如果初始化的时候Application实现了此接口，则框架会初始化数据库相关
 */
public interface SDatabaseDeclare {
    DatabaseInfo getDatabaseInfo();
}
