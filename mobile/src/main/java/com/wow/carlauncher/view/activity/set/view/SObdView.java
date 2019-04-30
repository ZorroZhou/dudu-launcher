package com.wow.carlauncher.view.activity.set.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.inuker.bluetooth.library.search.SearchResult;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.SharedPreUtil;
import com.wow.carlauncher.common.util.ThreadObj;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.ex.manage.ble.BleManage;
import com.wow.carlauncher.ex.manage.ble.BleSearch;
import com.wow.carlauncher.ex.manage.toast.ToastManage;
import com.wow.carlauncher.ex.plugin.fk.FangkongProtocolEnum;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.ObdProtocolEnum;
import com.wow.carlauncher.view.activity.set.SetEnumOnClickListener;
import com.wow.carlauncher.view.base.BaseView;
import com.wow.carlauncher.view.dialog.ListDialog;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.wow.carlauncher.common.CommonData.OBD_CONTROLLER;
import static com.wow.carlauncher.common.CommonData.SDATA_OBD_CONTROLLER;
import static com.wow.carlauncher.common.CommonData.SDATA_TRIP_AUTO_OPEN_DRIVING;
import static com.wow.carlauncher.common.CommonData.SDATA_TRIP_AUTO_OPEN_DRIVING_DF;

/**
 * Created by 10124 on 2018/4/22.
 */

public class SObdView extends BaseView {
    public static final FangkongProtocolEnum[] ALL_FANGKONG_CONTROLLER = {FangkongProtocolEnum.YLFK};

    public SObdView(@NonNull Context context) {
        super(context);
    }

    public SObdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getContent() {
        return R.layout.content_set_obd;
    }

    @ViewInject(R.id.sv_obd_select)
    private SetView sv_obd_select;

    @ViewInject(R.id.sv_obd_impl_select)
    private SetView sv_obd_impl_select;

    @ViewInject(R.id.sv_obd_disconnect)
    private SetView sv_obd_disconnect;

    @ViewInject(R.id.sv_auto_open_driving)
    private SetView sv_auto_open_driving;

    @Override
    protected void initView() {
        sv_obd_impl_select.setSummary("OBD使用的协议：" + ObdProtocolEnum.getById(SharedPreUtil.getInteger(SDATA_OBD_CONTROLLER, ObdProtocolEnum.YJ_TYB.getId())).getName());
        sv_obd_impl_select.setOnClickListener(new SetEnumOnClickListener<ObdProtocolEnum>(getContext(), OBD_CONTROLLER) {
            @Override
            public String title() {
                return "请选择OBD使用的协议";
            }

            @Override
            public ObdProtocolEnum getCurr() {
                return ObdProtocolEnum.getById(SharedPreUtil.getInteger(SDATA_OBD_CONTROLLER, ObdProtocolEnum.YJ_TYB.getId()));
            }

            @Override
            public void onSelect(ObdProtocolEnum setEnum) {
                SharedPreUtil.saveInteger(SDATA_OBD_CONTROLLER, setEnum.getId());
                sv_obd_impl_select.setSummary("OBD使用的协议：" + setEnum.getName());
                ObdPlugin.self().disconnect();
            }
        });


        sv_obd_disconnect.setOnClickListener(view -> {
            SharedPreUtil.saveString(CommonData.SDATA_OBD_ADDRESS, null);
            SharedPreUtil.saveString(CommonData.SDATA_OBD_NAME, null);
            ObdPlugin.self().disconnect();
            ToastManage.self().show("OBD绑定已删除");
            sv_obd_select.setSummary("没有绑定蓝牙设备");
        });


        String address = SharedPreUtil.getString(CommonData.SDATA_OBD_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            sv_obd_select.setSummary("绑定了设备:" + SharedPreUtil.getString(CommonData.SDATA_OBD_NAME) + "  地址:" + address);
        } else {
            sv_obd_select.setSummary("没有绑定蓝牙设备");
        }
        sv_obd_select.setOnClickListener(view -> {
            final ThreadObj<ListDialog> listTemp = new ThreadObj<>();
            final List<SearchResult> devices = new ArrayList<>();
            BleSearch bleSearch = new BleSearch() {
                public void findDevice(List<SearchResult> deviceList) {
                    for (SearchResult device : deviceList) {
                        boolean have = false;
                        for (SearchResult d : devices) {
                            if (d.getAddress().equals(device.getAddress())) {
                                have = true;
                                break;
                            }
                        }
                        if (!have) {
                            devices.add(device);
                        }
                    }

                    String[] items = new String[devices.size()];
                    for (int i = 0; i < items.length; i++) {
                        SearchResult bluetoothDevice = devices.get(i);
                        items[i] = bluetoothDevice.getName() + ":" + bluetoothDevice.getAddress();
                    }
                    x.task().autoPost(() -> listTemp.getObj().getListView().setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items)));
                }
            };
            final ListDialog dialog = new ListDialog(getContext());
            dialog.setOnDismissListener(dialogInterface -> bleSearch.destroy());

            dialog.setTitle("请选择一个蓝牙设备");
            dialog.show();
            listTemp.setObj(dialog);

            dialog.getListView().setOnItemClickListener((adapterView, view12, i, l) -> {
                dialog.dismiss();
                SearchResult device = devices.get(i);
                SharedPreUtil.saveString(CommonData.SDATA_OBD_ADDRESS, device.getAddress());
                SharedPreUtil.saveString(CommonData.SDATA_OBD_NAME, device.getName());
                sv_obd_select.setSummary("绑定了设备:" + device.getName() + "  地址:" + device.getAddress());
            });
        });

        sv_auto_open_driving.setOnValueChangeListener((newValue, oldValue) -> {
            if ("1".equals(newValue)) {
                SharedPreUtil.saveBoolean(SDATA_TRIP_AUTO_OPEN_DRIVING, true);
            } else {
                SharedPreUtil.saveBoolean(SDATA_TRIP_AUTO_OPEN_DRIVING, false);
            }
        });
        sv_auto_open_driving.setChecked(SharedPreUtil.getBoolean(SDATA_TRIP_AUTO_OPEN_DRIVING, SDATA_TRIP_AUTO_OPEN_DRIVING_DF));
    }
}
