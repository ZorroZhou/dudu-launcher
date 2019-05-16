package com.wow.carlauncher.view.activity.set;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.user.LocalUser;
import com.wow.carlauncher.common.user.event.UEventLoginState;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.AppWidgetManage;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonService;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;
import com.wow.carlauncher.view.activity.set.view.SDevView;
import com.wow.carlauncher.view.activity.set.view.SDrivingView;
import com.wow.carlauncher.view.activity.set.view.SFkView;
import com.wow.carlauncher.view.activity.set.view.SHomeView;
import com.wow.carlauncher.view.activity.set.view.SItemView;
import com.wow.carlauncher.view.activity.set.view.SLoadAppView;
import com.wow.carlauncher.view.activity.set.view.SObdView;
import com.wow.carlauncher.view.activity.set.view.SPopupView;
import com.wow.carlauncher.view.activity.set.view.SSystemView;
import com.wow.carlauncher.view.activity.set.view.SThemeView;
import com.wow.carlauncher.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_AMAP_PLUGIN;
import static com.wow.carlauncher.common.util.ViewUtils.getViewByIds;

public class SetActivity extends BaseActivity {
    @Override
    public void init() {
        setContent(R.layout.activity_set);
        if (AppContext.self().getLocalUser() == null) {
            loadQQOpen();
        }
    }

    @BindView(R.id.sg_theme)
    SetView sg_theme;

    @BindView(R.id.ll_set)
    FrameLayout ll_set;

    @BindView(R.id.ll_user)
    LinearLayout ll_user;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.iv_user_pic)
    ImageView iv_user_pic;

    private Tencent mTencent;

    @Override
    public void initView() {
        setTitle("设置");
        clickEvent(sg_theme);
        ll_user.setOnClickListener(v -> login());

        if (AppContext.self().getLocalUser() != null) {
            tv_nickname.setText(AppContext.self().getLocalUser().getNickname());
            ImageManage.self().loadImage(AppContext.self().getLocalUser().getUserPic(), iv_user_pic, new ImageSize(100, 100));
        }
    }

    @OnClick(value = {R.id.sg_dev, R.id.sg_item, R.id.sg_driving, R.id.sg_theme, R.id.sg_home, R.id.sg_obd, R.id.sg_fk, R.id.sg_load_app, R.id.sg_popup, R.id.sg_system_set})
    public void clickEvent(View view) {
        ViewGroup setView = null;
        switch (view.getId()) {
            case R.id.sg_theme: {
                setView = new SThemeView(this);
                break;
            }
            case R.id.sg_home: {
                setView = new SHomeView(this);
                break;
            }
            case R.id.sg_item: {
                setView = new SItemView(this);
                break;
            }
            case R.id.sg_obd: {
                setView = new SObdView(this);
                break;
            }
            case R.id.sg_fk: {
                setView = new SFkView(this);
                break;
            }
            case R.id.sg_load_app: {
                setView = new SLoadAppView(this);
                break;
            }
            case R.id.sg_popup: {
                setView = new SPopupView(this);
                break;
            }
            case R.id.sg_system_set: {
                setView = new SSystemView(this);
                break;
            }
            case R.id.sg_dev: {
                setView = new SDevView(this);
                break;
            }
            case R.id.sg_driving: {
                setView = new SDrivingView(this);
                break;
            }
        }
        if (setView != null) {
            ll_set.removeAllViews();
            ll_set.addView(setView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UEventLoginState event) {
        if (event.isLogin()) {
            tv_nickname.setText(AppContext.self().getLocalUser().getNickname());
            ImageManage.self().loadImage(AppContext.self().getLocalUser().getUserPic(), iv_user_pic);
        } else {
            loadQQOpen();
        }
    }

    private void loadQQOpen() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance("101580804", this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTencent != null) {
            mTencent.releaseResource();
        }
    }

    private void login() {
        mTencent.login(SetActivity.this, "all", new IUiListener() {

            @Override
            public void onComplete(Object response) {
                JSONObject loginResponse = (JSONObject) response;
                if (null == loginResponse || loginResponse.length() == 0) {
                    ToastManage.self().show("登陆失败:QQ互联授权失败1");
                    return;
                }
                try {
                    int ret = loginResponse.getInt("ret");
                    if (ret != 0) {
                        ToastManage.self().show("登陆失败:QQ互联授权失败2");
                        return;
                    }
                    IUiListener listener = new IUiListener() {
                        @Override
                        public void onComplete(final Object response) {
                            try {
                                JSONObject userInfoResponse = (JSONObject) response;
                                if (userInfoResponse == null || userInfoResponse.length() == 0) {
                                    ToastManage.self().show("登陆失败:QQ互联获取信息失败1");
                                    return;
                                }
                                int ret = userInfoResponse.getInt("ret");
                                if (ret != 0) {
                                    ToastManage.self().show("登陆失败:QQ互联获取信息失败2");
                                    return;
                                }
                                String accessToken = loginResponse.getString("access_token");
                                final String nickname = userInfoResponse.getString("nickname");
                                String userPic = userInfoResponse.getString("figureurl_qq_2");
                                if (CommonUtil.isNull(userPic)) {
                                    userPic = userInfoResponse.getString("figureurl_qq_1");
                                }
                                final String userPic2 = userPic;
                                CommonService.login(accessToken, nickname, userPic, (success, msg, loginInfo) -> {
                                    if (success == 0 && loginInfo.getId() != null && CommonUtil.isNotNull(loginInfo.getToken())) {
                                        ToastManage.self().show("登陆成功");
                                        AppContext.self().loginSuccess(new LocalUser().setUserId(loginInfo.getId()).setToken(loginInfo.getToken()).setUserPic(userPic2).setNickname(nickname));
                                    } else {
                                        ToastManage.self().show("登陆失败:" + msg);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastManage.self().show("登陆失败:数据错误!");
                            }
                        }

                        @Override
                        public void onCancel() {
                            ToastManage.self().show("登陆取消");
                        }

                        @Override
                        public void onError(UiError e) {
                            ToastManage.self().show("登陆失败:" + e.errorMessage);
                        }
                    };
                    UserInfo mInfo = new UserInfo(SetActivity.this, mTencent.getQQToken());
                    mInfo.getUserInfo(listener);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastManage.self().show("登陆失败:数据错误!");
                }
                //{"access_token":"F72D4071AFC79BA093A4A2CAE6A66545","expires_in":"7776000","openid":"063C55CF4D2682AB99AF299013630531","pay_token":"4C6F2192F952C2F7786390ED5C41352F","ret":"0","pf":"desktop_m_qq-10000144-android-2002-","pfkey":"da77473a10b765e298b3d34d57349565","auth_time":"1557991325082","page_type":"1","expires_time":1565767340312}
                //LogEx.d(SetActivity.this, "登陆成功,openid:" + jsonResponse.get("openid"));
                //{"ret":0,"msg":"","is_lost":0,"nickname":"Soap","gender":"男","province":"山东","city":"青岛","year":"1990","constellation":"","figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/101580804\/063C55CF4D2682AB99AF299013630531\/30","figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/101580804\/063C55CF4D2682AB99AF299013630531\/50","figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/101580804\/063C55CF4D2682AB99AF299013630531\/100","figureurl_qq_1":"http:\/\/thirdqq.qlogo.cn\/g?b=oidb&k=PWXibek9f9IplfjTEX7m1tA&s=40","figureurl_qq_2":"http:\/\/thirdqq.qlogo.cn\/g?b=oidb&k=PWXibek9f9IplfjTEX7m1tA&s=100","figureurl_qq":"http:\/\/thirdqq.qlogo.cn\/g?b=oidb&k=PWXibek9f9IplfjTEX7m1tA&s=140","figureurl_type":"1","is_yellow_vip":"0","vip":"0","yellow_vip_level":"0","level":"0","is_yellow_year_vip":"0"}
            }

            @Override
            public void onError(UiError e) {
                ToastManage.self().show("登陆失败:" + e.errorMessage);
            }

            @Override
            public void onCancel() {
                ToastManage.self().show("登陆取消");
            }
        }, true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_AMAP_PLUGIN: {
                    int id = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
                    boolean check = false;
                    if (id > 0) {
                        final View amapView = AppWidgetManage.self().getWidgetById(id);
                        View vv = getViewByIds(amapView, new Object[]{"widget_container", "daohang_container", 0, "gongban_daohang_right_blank_container", "daohang_widget_image"});
                        if (vv instanceof ImageView) {
                            check = true;
                        }
                    }
                    if (check) {
                        SharedPreUtil.saveInteger(APP_WIDGET_AMAP_PLUGIN, id);
                        EventBus.getDefault().post(new SEventRefreshAmapPlugin());
                    } else {
                        ToastManage.self().show("错误的插件!!");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}
