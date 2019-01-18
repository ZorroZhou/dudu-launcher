package com.wow.frame;

import com.google.gson.Gson;
import com.wow.frame.declare.SAppDeclare;
import com.wow.frame.declare.SDatabaseDeclare;
import com.wow.frame.declare.SWebServiceDeclare;
import com.wow.frame.repertory.dbTool.DatabaseManage;
import com.wow.frame.repertory.remote.WebServiceManage;
import com.wow.frame.util.SharedPreUtil;

import org.xutils.x;

/**
 * Created by 10124 on 2017/7/26.
 */
public class SFrame {
    private static boolean inited = false;

    private static Gson gson;

    public static void init(SAppDeclare context) {
        if (!inited) {
            inited = true;
            //初始化x框架
            x.Ext.init(context.getApplication());

            SharedPreUtil.init(context.getApplication());

            //如果需要初始化数据库，在这里初始化
            if (context instanceof SDatabaseDeclare) {
                SDatabaseDeclare declare = (SDatabaseDeclare) context;
                DatabaseManage.init(context.getApplication(), declare.getDatabaseInfo());
            }

            if (context instanceof SWebServiceDeclare) {
                SWebServiceDeclare declare = (SWebServiceDeclare) context;
                WebServiceManage.init(context.getApplication(), declare.getWebServiceInfo());
            }
        }
    }

    public static Gson getGson() {
        if (SFrame.gson == null) {
            SFrame.gson = new Gson();
        }
        return SFrame.gson;
    }
}
