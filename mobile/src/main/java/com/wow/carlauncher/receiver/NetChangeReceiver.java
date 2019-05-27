package com.wow.carlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.user.LocalUser;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.GsonUtil;
import com.wow.carlauncher.common.util.NetWorkUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonService;
import com.wow.carlauncher.view.event.EventNetStateChange;
import com.wow.carlauncher.view.event.EventWifiState;

import org.greenrobot.eventbus.EventBus;

import static com.wow.carlauncher.common.CommonData.LOGIN_USER_ID;
import static com.wow.carlauncher.common.CommonData.LOGIN_USER_INFO;
import static com.wow.carlauncher.repertory.server.ServerConstant.NET_ERROR;
import static com.wow.carlauncher.repertory.server.ServerConstant.RES_ERROR;

/**
 * Created by 10124 on 2017/10/31.
 */

public class NetChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            LogEx.d(this, "BootReceiver bootSuccess");
            // 接口回调传过去状态的类型
            if (NetWorkUtil.isWifiConnected(context)) {
                EventBus.getDefault().post(new EventWifiState().setUsable(true));
            } else {
                EventBus.getDefault().post(new EventWifiState().setUsable(false));
            }
            EventBus.getDefault().post(new EventNetStateChange());

            //如果在线,重新登录一下
            if (NetWorkUtil.isOnline()) {
                long uid = SharedPreUtil.getLong(LOGIN_USER_ID, -1);
                if (uid > 0) {
                    LocalUser user = GsonUtil.getGson().fromJson(SharedPreUtil.getString(LOGIN_USER_INFO), LocalUser.class);
                    if (user != null && CommonUtil.isNotNull(user.getToken())) {
                        CommonService.loginByToken(user.getToken(), (code, msg, loginInfo) -> {
                            if (code != NET_ERROR && code != RES_ERROR && code != 0) {
                                AppContext.self().logout();
                            } else {
                                if (code == 0) {
                                    AppContext.self().loginSuccess(new LocalUser().setUserId(loginInfo.getId()).setToken(loginInfo.getToken()).setUserPic(user.getUserPic()).setNickname(user.getNickname()).setEmail(loginInfo.getEmail()));
                                }
                            }
                        });
                    }
                }
            }
        }
    }
}
