package com.wow.carlauncher.view.activity.radios;

import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.view.PullToRefreshView;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.xmlyfm.XmlyfmPlugin;
import com.wow.carlauncher.view.base.BaseActivity;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province;
import com.ximalaya.ting.android.opensdk.model.live.provinces.ProvinceList;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class RadiosActivity extends BaseActivity {
    private int mRadioType = 1;
    private long mProvinceCode = 360000;

    private RadiosAdapter netRadioAdapter, myFavRadioAdapter;
    private ProvinceAdapter provinceAdapter;

    @Override
    public void init() {
        setContent(R.layout.activity_radios);
        netRadioAdapter = new RadiosAdapter(this);
        myFavRadioAdapter = new RadiosAdapter(this);
        provinceAdapter = new ProvinceAdapter(this);
    }

    @BindView(R.id.lv_radios)
    ListView lv_radios;

    @BindView(R.id.lv_my_radios)
    ListView lv_my_radios;

    @BindView(R.id.refresh_view)
    PullToRefreshView refresh_view;

    @BindView(R.id.tv_all)
    TextView tv_all;

    @Override
    public void initView() {
        setTitle("FM列表");
        lv_radios.setAdapter(netRadioAdapter);
        lv_radios.setOnItemClickListener((parent, view, position, id) -> {
            if (parent.getAdapter().equals(netRadioAdapter)) {
                XmlyfmPlugin.self().play(netRadioAdapter.getItem(position));
            } else {
                Province province = provinceAdapter.getItem(position);
                if (province.getProvinceCode() == -1L) {
                    mRadioType = 1;
                } else {
                    mRadioType = 2;
                    mProvinceCode = province.getProvinceCode();
                }
                page = 1;
                netRadioAdapter.clear();
                lv_radios.setAdapter(netRadioAdapter);
                loadRadios();
            }
        });
        lv_radios.setOnItemLongClickListener((parent, view, position, id) -> {
            if (parent.getAdapter().equals(netRadioAdapter)) {
                Radio radio = netRadioAdapter.getItem(position);
                XmlyfmPlugin.self().addRadio(radio);
                myFavRadioAdapter.addItem(radio);
                myFavRadioAdapter.notifyDataSetChanged();
                return true;
            }
            return false;
        });
        refresh_view.setOnFooterRefreshListener(view -> {
            if (page < totalPage) {
                page++;
                TaskExecutor.self().run(this::loadRadios, 1000);
            } else {
                refresh_view.onFooterRefreshComplete();
            }
        });
        refresh_view.setOnHeaderRefreshListener(view -> {
            page = 1;
            netRadioAdapter.clear();
            TaskExecutor.self().run(this::loadRadios, 1000);
        });


        lv_my_radios.setAdapter(myFavRadioAdapter);
        lv_my_radios.setOnItemClickListener((parent, view, position, id) -> XmlyfmPlugin.self().play(netRadioAdapter.getItem(position)));
        lv_my_radios.setOnItemLongClickListener((parent, view, position, id) -> {
            Radio radio = myFavRadioAdapter.getItem(position);
            XmlyfmPlugin.self().removeRadio(radio);
            myFavRadioAdapter.remove(radio);
            myFavRadioAdapter.notifyDataSetChanged();
            return true;
        });

        tv_all.setOnClickListener(v -> {
            lv_radios.setAdapter(provinceAdapter);
            loadProvince();
        });
    }

    @Override
    public void loadData() {
        myFavRadioAdapter.addItems(XmlyfmPlugin.self().getRadios());
        TaskExecutor.self().run(this::loadRadios);
    }

    private boolean mLoading = false;
    private int page = 1;
    private int totalPage = 1;

    private void loadProvince() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        showLoading("加载中");
        CommonRequest.getProvinces(null, new IDataCallBack<ProvinceList>() {
            @Override
            public void onSuccess(@Nullable ProvinceList provinceList) {
                if (provinceList != null && provinceList.getProvinceList() != null) {
                    provinceAdapter.clear();
                    Province province = new Province();
                    province.setProvinceName("国家台");
                    province.setProvinceCode(-1L);
                    provinceAdapter.addItem(province);
                    provinceAdapter.addItems(provinceList.getProvinceList());
                }
                hideLoading();
                mLoading = false;
            }

            @Override
            public void onError(int i, String message) {
                ToastManage.self().show(message);
                hideLoading();
                mLoading = false;
            }
        });
    }

    private void loadRadios() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        showLoading("加载中");
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.RADIOTYPE, "" + mRadioType);
        map.put(DTransferConstants.PROVINCECODE, "" + mProvinceCode);
        map.put(DTransferConstants.PAGE, "" + page);

        CommonRequest.getRadios(map, new IDataCallBack<RadioList>() {

            @Override
            public void onSuccess(RadioList object) {
                if (object != null && object.getRadios() != null) {
                    netRadioAdapter.addItems(object.getRadios());
                    totalPage = object.getTotalPage();
                }
                mLoading = false;
                if (page == 1) {
                    TaskExecutor.self().autoPost(() -> refresh_view.onHeaderRefreshComplete());
                } else {
                    TaskExecutor.self().autoPost(() -> refresh_view.onFooterRefreshComplete());
                }
                hideLoading();
            }

            @Override
            public void onError(int code, String message) {
                mLoading = false;
                ToastManage.self().show(message);
                TaskExecutor.self().autoPost(() -> {
                    refresh_view.onHeaderRefreshComplete();
                    refresh_view.onFooterRefreshComplete();
                });
                hideLoading();
            }
        });
    }

}
