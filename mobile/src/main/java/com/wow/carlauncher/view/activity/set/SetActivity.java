package com.wow.carlauncher.view.activity.set;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppContext;
import com.wow.carlauncher.common.LogEx;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.user.LocalUser;
import com.wow.carlauncher.common.user.event.UEventRefreshLoginState;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.AppWidgetManage;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.repertory.server.CommonService;
import com.wow.carlauncher.view.activity.set.event.SEventRefreshAmapPlugin;
import com.wow.carlauncher.view.activity.set.event.SEventRequestLogin;
import com.wow.carlauncher.view.activity.set.setComponent.SDevView;
import com.wow.carlauncher.view.activity.set.setComponent.SDrivingView;
import com.wow.carlauncher.view.activity.set.setComponent.SDuduView;
import com.wow.carlauncher.view.activity.set.setComponent.SFkView;
import com.wow.carlauncher.view.activity.set.setComponent.SHomeView;
import com.wow.carlauncher.view.activity.set.setComponent.SItemView;
import com.wow.carlauncher.view.activity.set.setComponent.SLoadAppView;
import com.wow.carlauncher.view.activity.set.setComponent.SObdView;
import com.wow.carlauncher.view.activity.set.setComponent.SPersionView;
import com.wow.carlauncher.view.activity.set.setComponent.SPopupView;
import com.wow.carlauncher.view.activity.set.setComponent.SSystemView;
import com.wow.carlauncher.view.activity.set.setComponent.SThemeView;
import com.wow.carlauncher.view.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wow.carlauncher.common.CommonData.APP_WIDGET_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.REQUEST_SELECT_AMAP_PLUGIN;
import static com.wow.carlauncher.common.CommonData.SDATA_HOME_FULL;
import static com.wow.carlauncher.common.util.ViewUtils.getViewByIds;

public class SetActivity extends BaseActivity implements SetFrame {
    @Override
    public void init() {
        if (SharedPreUtil.getBoolean(SDATA_HOME_FULL, true)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContent(R.layout.activity_set);
    }

    @BindView(R.id.sg_theme)
    SetView sg_theme;

    @BindView(R.id.set_content)
    FrameLayout set_content;

    @BindView(R.id.set_content_title)
    TextView set_content_title;

    @BindView(R.id.set_content_save)
    TextView set_content_save;

    @BindView(R.id.set_content_back)
    View set_content_back;

    @BindView(R.id.btn_back)
    View btn_back;

    private Tencent mTencent;

    @Override
    public void initView() {
        setTitle("设置");
        hideTitle();
        setBaseViews = new ArrayList<>();
        removeAllAndAddSetView(new SPersionView(this));

        View.OnClickListener onClickListener = v -> {
            switch (v.getId()) {
                case R.id.set_content_back:
                    backSetView();
                    break;
                case R.id.set_content_save:
                    if (setBaseViews.size() > 0) {
                        SetBaseView view = setBaseViews.get(setBaseViews.size() - 1);
                        if (view.rightAction()) {
                            backSetView();
                        }
                    }
                    break;
                case R.id.btn_back:
                    finish();
                    break;
            }
        };

        set_content_back.setOnClickListener(onClickListener);
        set_content_save.setOnClickListener(onClickListener);
        btn_back.setOnClickListener(onClickListener);

        TaskExecutor.self().run(() -> {
            if (AppContext.self().getLocalUser() == null) {
                showLoading("加载中...");
                loadQQOpen();
                hideLoading();
            }
        });
    }

    @OnClick(value = {R.id.sg_dev, R.id.sg_persion, R.id.sg_dudu, R.id.sg_item, R.id.sg_driving, R.id.sg_theme, R.id.sg_home, R.id.sg_obd, R.id.sg_fk, R.id.sg_load_app, R.id.sg_popup, R.id.sg_system_set})
    public void clickEvent(View view) {
        SetBaseView setView = null;
        switch (view.getId()) {
            case R.id.sg_persion: {
                setView = new SPersionView(this);
                break;
            }
            case R.id.sg_theme: {
                setView = new SThemeView(this);
                break;
            }
            case R.id.sg_home: {
                setView = new SHomeView(this);
                break;
            }
            case R.id.sg_dudu: {
                setView = new SDuduView(this);
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
            removeAllAndAddSetView(setView);
        }
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(UEventRefreshLoginState event) {
        if (!event.isLogin()) {
            loadQQOpen();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SEventRequestLogin event) {
        login();
    }


    private List<SetBaseView> setBaseViews;

    @Override
    public void removeAllAndAddSetView(SetBaseView view) {
        set_content.removeAllViews();
        setBaseViews.clear();
        addSetView(view);
    }

    @Override
    public void addSetView(SetBaseView view) {
        if (setBaseViews.size() > 0) {
            for (SetBaseView view1 : setBaseViews) {
                view1.setVisibility(View.GONE);
            }
        }
        setBaseViews.add(view);
        set_content.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        showSetView(view);
    }

    @Override
    public void backSetView() {
        if (setBaseViews.size() > 1) {
            SetBaseView view = setBaseViews.get(setBaseViews.size() - 1);
            set_content.removeView(view);
            setBaseViews.remove(view);
            view = setBaseViews.get(setBaseViews.size() - 1);
            view.setVisibility(View.VISIBLE);
            showSetView(view);
        }
    }

    private void showSetView(SetBaseView view) {
        set_content_title.setText(view.getName());
        if (view.showRight()) {
            set_content_save.setVisibility(View.VISIBLE);
            set_content_save.setText(view.rightTitle());
        } else {
            set_content_save.setVisibility(View.INVISIBLE);
        }
        if (setBaseViews.size() > 1) {
            set_content_back.setVisibility(View.VISIBLE);
        } else {
            set_content_back.setVisibility(View.INVISIBLE);
        }
    }


    private void loadQQOpen() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance("101580804", getApplicationContext());
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
        showLoading("处理中...");
        System.out.println("!!!1" + mTencent.isQQInstalled(SetActivity.this));
        mTencent.login(SetActivity.this, "all", loginListener, !mTencent.isQQInstalled(SetActivity.this));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        } else if (resultCode == RESULT_OK) {
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

    private IUiListener loginListener = new IUiListener() {

        @Override
        public void onComplete(Object response) {
            JSONObject loginResponse = (JSONObject) response;
            if (null == loginResponse || loginResponse.length() == 0) {
                LogEx.e(SetActivity.this, loginResponse);
                ToastManage.self().show("登陆失败:QQ互联授权失败1");
                hideLoading();
                return;
            }
            try {
                int ret = loginResponse.getInt("ret");
                if (ret != 0) {
                    LogEx.e(SetActivity.this, loginResponse);
                    ToastManage.self().show("登陆失败:QQ互联授权失败2");
                    hideLoading();
                    return;
                }
                String accessToken = loginResponse.getString("access_token");
                if (CommonUtil.isNull(accessToken)) {
                    LogEx.e(SetActivity.this, loginResponse);
                    ToastManage.self().show("登陆失败:QQ互联授权失败3");
                    hideLoading();
                    return;
                }
                //坑货QQ,这里竟然必须自己设置
                mTencent.setOpenId(loginResponse.getString("openid"));
                mTencent.setAccessToken(accessToken, loginResponse.getString("expires_in"));
                IUiListener listener = new IUiListener() {
                    @Override
                    public void onComplete(final Object response) {
                        try {
                            JSONObject userInfoResponse = (JSONObject) response;
                            if (userInfoResponse == null || userInfoResponse.length() == 0) {
                                LogEx.e(SetActivity.this, userInfoResponse);
                                ToastManage.self().show("登陆失败:QQ互联获取信息失败1");
                                hideLoading();
                                return;
                            }
                            int ret = userInfoResponse.getInt("ret");
                            if (ret != 0) {
                                LogEx.e(SetActivity.this, userInfoResponse);
                                ToastManage.self().show("登陆失败:QQ互联获取信息失败2");
                                hideLoading();
                                return;
                            }
                            final String nickname = userInfoResponse.getString("nickname");
                            String userPic = userInfoResponse.getString("figureurl_qq_2");
                            if (CommonUtil.isNull(userPic)) {
                                userPic = userInfoResponse.getString("figureurl_qq_1");
                            }
                            final String userPic2 = userPic;
                            CommonService.login(accessToken, nickname, userPic, (success, msg, loginInfo) -> {
                                if (success == 0 && loginInfo.getId() != null && CommonUtil.isNotNull(loginInfo.getToken())) {
                                    ToastManage.self().show("登陆成功");
                                    AppContext.self().loginSuccess(new LocalUser().setUserId(loginInfo.getId()).setToken(loginInfo.getToken()).setUserPic(userPic2).setNickname(nickname).setEmail(loginInfo.getEmail()));
                                } else {
                                    hideLoading();
                                    ToastManage.self().show("登陆失败:" + msg);
                                }
                                hideLoading();
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideLoading();
                            ToastManage.self().show("登陆失败:数据错误!");
                        }
                    }

                    @Override
                    public void onCancel() {
                        hideLoading();
                        ToastManage.self().show("登陆取消");
                    }

                    @Override
                    public void onError(UiError e) {
                        hideLoading();
                        ToastManage.self().show("登陆失败:" + e.errorMessage);
                    }
                };
                UserInfo mInfo = new UserInfo(SetActivity.this, mTencent.getQQToken());
                mInfo.getUserInfo(listener);
            } catch (Exception e) {
                e.printStackTrace();
                ToastManage.self().show("登陆失败:数据错误!");
                hideLoading();
            }
        }

        @Override
        public void onError(UiError e) {
            hideLoading();
            ToastManage.self().show("登陆失败:" + e.errorMessage);
        }

        @Override
        public void onCancel() {
            hideLoading();
            ToastManage.self().show("登陆取消");
        }
    };
}
