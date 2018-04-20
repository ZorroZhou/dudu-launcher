package com.wow.carlauncher.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wow.carAssistant.packet.response.common.GetAppUpdateRes;
import com.wow.carlauncher.dialog.ListDialog;
import com.wow.carlauncher.plugin.console.ConsoleProtoclEnum;
import com.wow.carlauncher.plugin.fk.FangkongPlugin;
import com.wow.carlauncher.plugin.fk.FangkongProtocolEnum;
import com.wow.carlauncher.plugin.music.MusicControllerEnum;
import com.wow.carlauncher.plugin.music.MusicPlugin;
import com.wow.carlauncher.plugin.obd.ObdPlugin;
import com.wow.carlauncher.plugin.obd.ObdProtocolEnum;
import com.wow.carlauncher.webservice.service.CommonService;
import com.wow.frame.repertory.remote.WebServiceManage;
import com.wow.frame.repertory.remote.WebTask;
import com.wow.frame.repertory.remote.callback.SCallBack;
import com.wow.frame.repertory.remote.callback.SProgressCallback;
import com.wow.frame.util.AndroidUtil;
import com.wow.frame.util.CommonUtil;
import com.wow.frame.util.SharedPreUtil;
import com.wow.carlauncher.R;
import com.wow.carlauncher.common.AppInfoManage;
import com.wow.carlauncher.common.BaseActivity;
import com.wow.carlauncher.common.BaseDialog;
import com.wow.carlauncher.common.CommonData;
import com.wow.carlauncher.plugin.console.ConsolePlugin;
import com.wow.carlauncher.plugin.console.impl.NwdConsoleImpl;
import com.wow.carlauncher.plugin.console.impl.SysConsoleImpl;
import com.wow.frame.util.AppUtil.AppInfo;
import com.wow.carlauncher.common.view.SetView;
import com.wow.carlauncher.dialog.CityDialog;
import com.wow.carlauncher.event.LauncherCityRefreshEvent;
import com.wow.carlauncher.event.LauncherDockLabelShowChangeEvent;
import com.wow.carlauncher.event.PopupIsFullScreenRefreshEvent;
import com.wow.carlauncher.popupWindow.PopupWin;
import com.wow.frame.util.ThreadObj;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_DUAL;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE;
import static com.wow.carlauncher.common.CommonData.*;

public class SetActivity extends BaseActivity {
    private static final String[] CONSOLES = {"系统", "NWD"};
    public static final MusicControllerEnum[] ALL_MUSIC_CONTROLLER = {MusicControllerEnum.SYSMUSIC,
            MusicControllerEnum.QQCARMUSIC,
            MusicControllerEnum.JIDOUMUSIC,
            MusicControllerEnum.POWERAMPMUSIC,
            MusicControllerEnum.NWDMUSIC};
    public static final FangkongProtocolEnum[] ALL_FANGKONG_CONTROLLER = {FangkongProtocolEnum.YLFK};

    public static final ObdProtocolEnum[] ALL_OBD_CONTROLLER = {ObdProtocolEnum.YJ_TYB};

    @Override
    public void init() {
        setContent(R.layout.activity_set);
    }

    @Override
    public void initView() {
        setTitle("设置");
        loadSetGroup();
        loadAppSet();
        loadFangkongSet();
        loadPopupSet();
        loadTimeSet();
        loadHelpSet();
        loadObdSet();
        loadSystemSet();
    }

    @ViewInject(R.id.sg_app)
    private SetView sg_app;
    @ViewInject(R.id.sg_popup)
    private SetView sg_popup;
    @ViewInject(R.id.sg_time)
    private SetView sg_time;
    @ViewInject(R.id.sg_help)
    private SetView sg_help;
    @ViewInject(R.id.sg_system_set)
    private SetView sg_system_set;
    @ViewInject(R.id.sg_fangkong)
    private SetView sg_fangkong;
    @ViewInject(R.id.sg_obd)
    private SetView sg_obd;

    @ViewInject(R.id.ll_app)
    private LinearLayout ll_app;
    @ViewInject(R.id.ll_popup)
    private LinearLayout ll_popup;
    @ViewInject(R.id.ll_time)
    private LinearLayout ll_time;
    @ViewInject(R.id.ll_help)
    private LinearLayout ll_help;
    @ViewInject(R.id.ll_system_set)
    private LinearLayout ll_system_set;
    @ViewInject(R.id.ll_fangkong)
    private LinearLayout ll_fangkong;
    @ViewInject(R.id.ll_obd)
    private LinearLayout ll_obd;

    private void loadSetGroup() {
        View.OnClickListener groupClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_app.setVisibility(View.GONE);
                ll_popup.setVisibility(View.GONE);
                ll_time.setVisibility(View.GONE);
                ll_help.setVisibility(View.GONE);
                ll_system_set.setVisibility(View.GONE);
                ll_fangkong.setVisibility(View.GONE);
                ll_obd.setVisibility(View.GONE);

                switch (view.getId()) {
                    case R.id.sg_app: {
                        ll_app.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_popup: {
                        ll_popup.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_time: {
                        ll_time.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_fangkong: {
                        ll_fangkong.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_help: {
                        ll_help.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_system_set: {
                        ll_system_set.setVisibility(View.VISIBLE);
                        break;
                    }
                    case R.id.sg_obd: {
                        ll_obd.setVisibility(View.VISIBLE);
                        break;
                    }
                    default: {
                        ll_app.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        sg_app.setOnClickListener(groupClick);
        sg_popup.setOnClickListener(groupClick);
        sg_time.setOnClickListener(groupClick);
        sg_help.setOnClickListener(groupClick);
        sg_system_set.setOnClickListener(groupClick);
        sg_fangkong.setOnClickListener(groupClick);
        sg_obd.setOnClickListener(groupClick);
    }

    @ViewInject(R.id.sv_plugin_select)
    private SetView sv_plugin_select;

    @ViewInject(R.id.sv_plugin_set)
    private SetView sv_plugin_set;

    @ViewInject(R.id.sv_wall_set)
    private SetView sv_wall_set;

    @ViewInject(R.id.sv_apps_hides)
    private SetView sv_apps_hides;

    @ViewInject(R.id.sv_console)
    private SetView sv_console;

    @ViewInject(R.id.sv_launcher_show_dock_label)
    private SetView sv_launcher_show_dock_label;

    private void loadAppSet() {

        MusicControllerEnum p1 = MusicControllerEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId()));
        sv_plugin_select.setSummary("音乐播放器使用的控制器：" + p1.getName());
        sv_plugin_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicControllerEnum p = MusicControllerEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_MUSIC_CONTROLLER, MusicControllerEnum.SYSMUSIC.getId()));
                final MusicControllerEnum[] show = ALL_MUSIC_CONTROLLER;
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择插件").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_MUSIC_CONTROLLER, show[obj.getObj()].getId());
                        MusicPlugin.self().setController(show[obj.getObj()]);
                        sv_plugin_select.setSummary("音乐播放器使用的控制器：" + show[obj.getObj()].getName());
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });


        sv_plugin_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, PluginSetActivity.class));
            }
        });
        sv_wall_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTip("请使用第三方看图软件直接将图片设置为安卓壁纸即可");
            }
        });
        sv_apps_hides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_HIDE_APPS);
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
                String[] items = new String[appInfos.size()];
                final boolean[] checks = new boolean[appInfos.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    checks[i] = selectapp.contains("[" + appInfos.get(i).packageName + "]");
                }

                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectapp = "";
                        for (int i = 0; i < appInfos.size(); i++) {
                            if (checks[i]) {
                                selectapp = selectapp + "[" + appInfos.get(i).packageName + "];";
                            }
                        }
                        if (selectapp.endsWith(";")) {
                            selectapp = selectapp.substring(0, selectapp.length() - 1);
                        }
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_HIDE_APPS, selectapp);
                    }
                }).setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.e(TAG, "onClick: " + appInfos.get(which).name);
                        checks[which] = isChecked;
                    }
                }).create();
                dialog.show();
            }
        });

        sv_console.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int select = SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, SysConsoleImpl.MARK);
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择控制器").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (obj.getObj()) {
                            case SysConsoleImpl.MARK: {
                                SharedPreUtil.saveSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.SYSTEM.getId());
                                break;
                            }
                            case NwdConsoleImpl.MARK: {
                                SharedPreUtil.saveSharedPreInteger(SDATA_CONSOLE_MARK, ConsoleProtoclEnum.NWD.getId());
                                break;
                            }
                        }
                    }
                }).setSingleChoiceItems(CONSOLES, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });
        sv_console.setSummary(CONSOLES[SharedPreUtil.getSharedPreInteger(SDATA_CONSOLE_MARK, SysConsoleImpl.MARK)]);

        sv_launcher_show_dock_label.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true);
                    EventBus.getDefault().post(new LauncherDockLabelShowChangeEvent(true));
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, false);
                    EventBus.getDefault().post(new LauncherDockLabelShowChangeEvent(false));
                }
            }
        });
        sv_launcher_show_dock_label.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_LAUNCHER_DOCK_LABEL_SHOW, true));
    }


    @ViewInject(R.id.sv_fangkong_select)
    private SetView sv_fangkong_select;

    @ViewInject(R.id.sv_fangkong_impl_select)
    private SetView sv_fangkong_impl_select;

    @ViewInject(R.id.sv_fangkong_disconnect)
    private SetView sv_fangkong_disconnect;


    private void loadFangkongSet() {
        FangkongProtocolEnum p1 = FangkongProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_FANGKONG_CONTROLLER, FangkongProtocolEnum.YLFK.getId()));
        sv_fangkong_impl_select.setSummary("方控使用的协议：" + p1.getName());
        sv_fangkong_impl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FangkongProtocolEnum p = FangkongProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_FANGKONG_CONTROLLER, FangkongProtocolEnum.YLFK.getId()));
                final FangkongProtocolEnum[] show = ALL_FANGKONG_CONTROLLER;
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择协议").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_FANGKONG_CONTROLLER, show[obj.getObj()].getId());
                        FangkongPlugin.self().connect();
                        sv_plugin_select.setSummary("方控使用的协议：" + show[obj.getObj()].getName());
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });


        sv_fangkong_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FangkongPlugin.self().disconnect();
            }
        });


        String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            sv_fangkong_select.setSummary("绑定了设备:" + SharedPreUtil.getSharedPreString(CommonData.SDATA_FANGKONG_NAME) + "  地址:" + address);
        } else {
            sv_fangkong_select.setSummary("没有绑定蓝牙设备");
        }
        sv_fangkong_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ThreadObj<ListDialog> listTemp = new ThreadObj<>();

                final List<BluetoothDevice> devices = new ArrayList<>();
                BluetoothManager mBluetoothManager = (BluetoothManager) getApplication().getSystemService(Context.BLUETOOTH_SERVICE);
                final BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
                final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(BluetoothDevice bluetoothDevice, int index, byte[] bytes) {
                        if (bluetoothDevice != null)
                            System.out.println(bluetoothDevice.getName() + "--" + bluetoothDevice.getAddress() + " " + bluetoothDevice.getType());
                        if (bluetoothDevice == null || bluetoothDevice.getName() == null || (bluetoothDevice.getType() != DEVICE_TYPE_LE && bluetoothDevice.getType() != DEVICE_TYPE_DUAL)) {
                            return;
                        }
                        boolean have = false;
                        for (BluetoothDevice device : devices) {
                            if (device.getAddress().equals(device.getAddress())) {
                                have = true;
                                break;
                            }
                        }
                        if (!have) {
                            devices.add(bluetoothDevice);
                            String[] items = new String[devices.size()];
                            for (int i = 0; i < items.length; i++) {
                                items[i] = devices.get(i).getName() + ":" + devices.get(i).getAddress();
                            }
                            listTemp.getObj().getListView().setAdapter(new ArrayAdapter<String>(SetActivity.this, android.R.layout.simple_list_item_1, items));
                        }
                    }
                };
                final ListDialog dialog = new ListDialog(mContext);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        bluetoothAdapter.stopLeScan(callback);
                    }
                });
                dialog.setTitle("请选择一个蓝牙设备");
                dialog.show();
                listTemp.setObj(dialog);
                bluetoothAdapter.startLeScan(callback);

                dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialog.dismiss();
                        BluetoothDevice device = devices.get(i);
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_FANGKONG_ADDRESS, device.getAddress());
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_FANGKONG_NAME, device.getName());

                        sv_fangkong_select.setSummary("绑定了设备:" + device.getName() + "  地址:" + device.getAddress());

                        FangkongPlugin.self().connect();
                    }
                });
            }
        });
    }

    @ViewInject(R.id.sv_obd_select)
    private SetView sv_obd_select;

    @ViewInject(R.id.sv_obd_impl_select)
    private SetView sv_obd_impl_select;

    @ViewInject(R.id.sv_obd_disconnect)
    private SetView sv_obd_disconnect;


    private void loadObdSet() {
        ObdProtocolEnum p1 = ObdProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_OBD_CONTROLLER, ObdProtocolEnum.YJ_TYB.getId()));
        sv_obd_impl_select.setSummary("OBD使用的协议：" + p1.getName());
        sv_obd_impl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObdProtocolEnum p = ObdProtocolEnum.getById(SharedPreUtil.getSharedPreInteger(SDATA_OBD_CONTROLLER, ObdProtocolEnum.YJ_TYB.getId()));
                final ObdProtocolEnum[] show = ALL_OBD_CONTROLLER;
                String[] items = new String[show.length];
                int select = 0;
                for (int i = 0; i < show.length; i++) {
                    items[i] = show[i].getName();
                    if (show[i].equals(p)) {
                        select = i;
                    }
                }
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择协议").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreInteger(SDATA_OBD_CONTROLLER, show[obj.getObj()].getId());
                        sv_plugin_select.setSummary("OBD使用的协议：" + show[obj.getObj()].getName());
                        ObdPlugin.self().start();
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });


        sv_obd_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObdPlugin.self().stop();
            }
        });


        String address = SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_ADDRESS);
        if (CommonUtil.isNotNull(address)) {
            sv_obd_select.setSummary("绑定了设备:" + SharedPreUtil.getSharedPreString(CommonData.SDATA_OBD_NAME) + "  地址:" + address);
        } else {
            sv_obd_select.setSummary("没有绑定蓝牙设备");
        }
        sv_obd_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ThreadObj<ListDialog> listTemp = new ThreadObj<>();

                final List<BluetoothDevice> devices = new ArrayList<>();
                BluetoothManager mBluetoothManager = (BluetoothManager) getApplication().getSystemService(Context.BLUETOOTH_SERVICE);
                final BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
                final BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(BluetoothDevice bluetoothDevice, int index, byte[] bytes) {
                        if (bluetoothDevice != null)
                            System.out.println(bluetoothDevice.getName() + "--" + bluetoothDevice.getAddress() + " " + bluetoothDevice.getType());
                        if (bluetoothDevice == null || bluetoothDevice.getName() == null || (bluetoothDevice.getType() != DEVICE_TYPE_LE && bluetoothDevice.getType() != DEVICE_TYPE_DUAL)) {
                            return;
                        }
                        boolean have = false;
                        for (BluetoothDevice device : devices) {
                            if (device.getAddress().equals(device.getAddress())) {
                                have = true;
                                break;
                            }
                        }
                        if (!have) {
                            devices.add(bluetoothDevice);
                            String[] items = new String[devices.size()];
                            for (int i = 0; i < items.length; i++) {
                                items[i] = devices.get(i).getName() + ":" + devices.get(i).getAddress();
                            }
                            listTemp.getObj().getListView().setAdapter(new ArrayAdapter<String>(SetActivity.this, android.R.layout.simple_list_item_1, items));
                        }
                    }
                };
                final ListDialog dialog = new ListDialog(mContext);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        bluetoothAdapter.stopLeScan(callback);
                    }
                });
                dialog.setTitle("请选择一个蓝牙设备");
                dialog.show();
                listTemp.setObj(dialog);
                bluetoothAdapter.startLeScan(callback);

                dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialog.dismiss();
                        BluetoothDevice device = devices.get(i);
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_OBD_ADDRESS, device.getAddress());
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_OBD_NAME, device.getName());

                        sv_obd_select.setSummary("绑定了设备:" + device.getName() + "  地址:" + device.getAddress());

                        ObdPlugin.self().start();
                    }
                });
            }
        });
    }


    @ViewInject(R.id.sv_allow_popup_window)
    private SetView sv_allow_popup_window;

    @ViewInject(R.id.sv_popup_window_showapps)
    private SetView sv_popup_window_showapps;

    @ViewInject(R.id.sv_popup_window_showtype)
    private SetView sv_popup_window_showtype;

    @ViewInject(R.id.sv_popup_full_screen)
    private SetView sv_popup_full_screen;

    @ViewInject(R.id.sv_popup_window_size)
    private SetView sv_popup_window_size;

    private void loadPopupSet() {
        sv_popup_window_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setItems(POPUP_SIZE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreUtil.saveSharedPreInteger(CommonData.SDATA_POPUP_SIZE, i);
                        PopupWin.self().setRank(i + 1);
                    }
                }).create();
                dialog.show();
            }
        });
        sv_popup_window_size.setSummary(POPUP_SIZE[SharedPreUtil.getSharedPreInteger(CommonData.SDATA_POPUP_SIZE, 1)]);

        sv_popup_full_screen.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                Log.e(TAG, "onValueChange: " + newValue);
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, false);
                }
                EventBus.getDefault().post(new PopupIsFullScreenRefreshEvent());
            }
        });
        sv_popup_full_screen.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_FULL_SCREEN, true));

        sv_allow_popup_window.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, false);
                }
            }
        });
        sv_allow_popup_window.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_ALLOW_SHOW, true));

        sv_popup_window_showapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS);
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
                String[] items = new String[appInfos.size()];
                final boolean[] checks = new boolean[appInfos.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    checks[i] = selectapp.contains("[" + appInfos.get(i).packageName + "]");
                }

                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectapp = "";
                        for (int i = 0; i < appInfos.size(); i++) {
                            if (checks[i]) {
                                selectapp = selectapp + "[" + appInfos.get(i).packageName + "];";
                            }
                        }
                        if (selectapp.endsWith(";")) {
                            selectapp = selectapp.substring(0, selectapp.length() - 1);
                        }
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_POPUP_SHOW_APPS, selectapp);
                    }
                }).setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.e(TAG, "onClick: " + appInfos.get(which).name);
                        checks[which] = isChecked;
                    }
                }).create();
                dialog.show();
            }
        });


        sv_popup_window_showtype.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, false);
                }
            }
        });
        sv_popup_window_showtype.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_POPUP_SHOW_TYPE, true));

    }


    @ViewInject(R.id.time_plugin_open_app_select)
    private SetView time_plugin_open_app_select;


    @ViewInject(R.id.tianqi_city)
    private SetView tianqi_city;

    private void loadTimeSet() {
        time_plugin_open_app_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectapp = SharedPreUtil.getSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP);
                final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
                String[] items = new String[appInfos.size()];
                int select = -1;
                for (int i = 0; i < items.length; i++) {
                    items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    if (appInfos.get(i).packageName.equals(selectapp)) {
                        select = i;
                    }
                }
                Log.e(TAG, "onClick: " + items.length + " " + select);
                final ThreadObj<Integer> obj = new ThreadObj<>(select);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreUtil.saveSharedPreString(CommonData.SDATA_TIME_PLUGIN_OPEN_APP, appInfos.get(obj.getObj()).packageName);
                    }
                }).setSingleChoiceItems(items, select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        obj.setObj(which);
                    }
                }).create();
                dialog.show();
            }
        });

        tianqi_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CityDialog cityDialog = new CityDialog(mContext);
                cityDialog.setOkListener(new BaseDialog.OnBtnClickListener() {
                    @Override
                    public boolean onClick(BaseDialog dialog) {
                        Log.e(TAG, "onClick: " + cityDialog.getmCurrentDistrictName());
                        if (CommonUtil.isNotNull(cityDialog.getmCurrentDistrictName())) {
                            SharedPreUtil.saveSharedPreString(CommonData.SDATA_WEATHER_CITY, cityDialog.getmCurrentDistrictName());
                            dialog.dismiss();
                            EventBus.getDefault().post(new LauncherCityRefreshEvent());
                            tianqi_city.setSummary(cityDialog.getmCurrentDistrictName());
                        } else {
                            showTip("请选择城市");
                        }
                        return false;
                    }
                });
                cityDialog.show();
            }
        });
        tianqi_city.setSummary(SharedPreUtil.getSharedPreString(CommonData.SDATA_WEATHER_CITY));
    }

    @ViewInject(R.id.sv_about)
    private SetView sv_about;

    @ViewInject(R.id.sv_money)
    private SetView sv_money;

    @ViewInject(R.id.sv_get_newest)
    private SetView sv_get_newest;

    @ViewInject(R.id.sv_get_debug_newest)
    private SetView sv_get_debug_newest;

    private WebTask<File> downloadUpdataTask;

    private ProgressInterruptListener downloadUpdataTaskCancel = new ProgressInterruptListener() {
        @Override
        public void onProgressInterruptListener(ProgressDialog progressDialog) {
            if (downloadUpdataTask != null) {
                downloadUpdataTask.cancelTask();
            }
        }
    };

    private void loadHelpSet() {
        sv_get_debug_newest.setOnValueChangeListener(new SetView.OnValueChangeListener() {
            @Override
            public void onValueChange(String newValue, String oldValue) {
                Log.e(TAG, "onValueChange: " + newValue);
                if ("1".equals(newValue)) {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, true);
                } else {
                    SharedPreUtil.saveSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false);
                }
            }
        });
        sv_get_debug_newest.setChecked(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false));

        sv_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AboutActivity.class));
            }
        });

        sv_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = new ImageView(mContext);
                imageView.setImageResource(R.mipmap.money);
                AlertDialog dialog = new AlertDialog.Builder(mContext).setView(imageView).setTitle("支持我吧!").setPositiveButton("确定", null).create();
                dialog.show();
            }
        });

        sv_get_newest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(SetActivity.this);
                WebServiceManage.getService(CommonService.class).checkUpdate(SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false)).setCallback(new SCallBack<GetAppUpdateRes>() {
                    @Override
                    public void callback(boolean isok, String msg, final GetAppUpdateRes res) {
                        if (isok) {
                            final String version = AndroidUtil.getAppVersionCode(mContext);
                            if (TextUtils.isEmpty(version)) {
                                showTip("获取当前版本号失败");
                                return;
                            }
                            if (res.getVersionCode() == null) {
                                showTip("没有新版本发布");
                                return;
                            }
                            boolean havenew;
                            Log.e(TAG, "callback!!!!!!!!!!!!!!!: " + SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false));
                            Log.e(TAG, "callback!!!!!!!!!!!!!!!: " + (res.getVersionCode() >= Integer.parseInt(version)));
                            if (SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false)) {
                                havenew = res.getVersionCode() >= Integer.parseInt(version);
                            } else {
                                havenew = res.getVersionCode() > Integer.parseInt(version);
                            }

                            if (havenew) {
                                BaseDialog baseDialog = new BaseDialog(mContext);
                                baseDialog.setGravityCenter();
                                baseDialog.setTitle("版本更新");
                                baseDialog.setMessage("最新版本为" + res.getVersionName() + ",是否更新?\n" + res.getNewMessage());
                                baseDialog.setBtn1("下载", new BaseDialog.OnBtnClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog dialog) {
                                        dialog.dismiss();
                                        final String savePath = Environment.getExternalStorageDirectory() + "/" + res.getVersionName() + ".apk";
                                        downloadUpdataTask = WebServiceManage.getService(CommonService.class).getAppUpdateFile(savePath, SharedPreUtil.getSharedPreBoolean(CommonData.SDATA_ALLOW_DEBUG_APP, false));
                                        showLoading("开始下载!", downloadUpdataTaskCancel);
                                        downloadUpdataTask.setCallback(new SProgressCallback<File>() {
                                            @Override
                                            public void onProgress(float progress) {
                                                if (progress > 0 && progress < 1)
                                                    downloadResult(progress, savePath, null);
                                            }

                                            @Override
                                            public void callback(boolean isok, String msg, File res) {
                                                if (isok) {
                                                    downloadResult(1f, savePath, savePath);
                                                } else {
                                                    downloadResult(-1f, savePath, msg);
                                                }
                                            }
                                        });
                                        return true;
                                    }
                                });
                                baseDialog.setBtn2("取消", null);
                                baseDialog.show();
                            } else {
                                showTip("已是最新版本");
                            }

                        } else {
                            showTip("错误:" + msg);
                        }
                    }
                });
            }
        });
    }

    public void downloadResult(float progress, String path, String msg) {
        if (progress == -1) {
            showTip(msg);
            return;
        }
        if (progress >= 0 && progress < 1) {
            showLoading("已经下载:" + (int) (progress * 100) + "%", downloadUpdataTaskCancel);
        }
        if (progress == 1) {
            showTip("下载成功");
            downloadUpdataTask = null;
            hideLoading();
            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            Uri data = Uri.fromFile(new File(path + ".tmp"));
            intent.setDataAndType(data, "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    @ViewInject(R.id.sv_sys_anquan)
    private SetView sv_sys_anquan;

    @ViewInject(R.id.sv_sys_overlay)
    private SetView sv_sys_overlay;

    @ViewInject(R.id.sv_sys_sdk)
    private SetView sv_sys_sdk;

    private void loadSystemSet() {
        sv_sys_overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    final List<AppInfo> appInfos = new ArrayList<>(AppInfoManage.self().getAppInfos());
                    String[] items = new String[appInfos.size()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = appInfos.get(i).name + "(" + appInfos.get(i).packageName + ")";
                    }

                    AlertDialog dialog = new AlertDialog.Builder(mContext).setTitle("请选择APP").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + appInfos.get(which).packageName));
                            startActivity(intent);
                        }
                    }).create();
                    dialog.show();
                } else {
                    showTip("这个功能是安卓6.0以上才有的");
                }
            }
        });

        sv_sys_anquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivity(intent);
            }
        });

        sv_sys_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTip("当前SDK版本是" + Build.VERSION.SDK_INT);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadUpdataTask != null)
            downloadUpdataTask.cancelTask();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * * Checks if the app has permission to write to device storage
     * *
     * * If the app does not has permission then the user will be prompted to
     * * grant permissions
     * *
     * * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}
