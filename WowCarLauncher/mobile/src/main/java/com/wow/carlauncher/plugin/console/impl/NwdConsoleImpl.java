package com.wow.carlauncher.plugin.console.impl;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;

import com.wow.carlauncher.common.ex.ToastManage;
import com.wow.carlauncher.plugin.console.ConsoleProtoclListener;
import com.wow.carlauncher.plugin.console.ConsoleProtocl;

import static com.wow.carlauncher.common.CommonData.TAG;
import static com.wow.carlauncher.plugin.console.impl.NwdConsoleImpl.PublicSettingTable.NO_SOURCE_DEVICE_STATE;

/**
 * Created by 10124 on 2017/11/9.
 */

public class NwdConsoleImpl extends ConsoleProtocl {
    public static final String ACTION_BT_BEGIN_CALL_ONLINE = "com.bt.ACTION_BT_BEGIN_CALL_ONLINE";
    public static final String ACTION_BT_END_CALL = "com.bt.ACTION_BT_END_CALL";
    public static final String ACTION_BT_INCOMING_CALL = "com.bt.ACTION_BT_INCOMING_CALL";
    public static final String ACTION_BT_OUTGOING_CALL = "com.bt.ACTION_BT_OUTGOING_CALL";

    public static final int MARK = 1;

    private Intent mSetVolumeIntent = new Intent("com.nwd.action.ACTION_KEY_VALUE");
    private Intent mMuteIntent = new Intent("com.nwd.action.ACTION_SET_MUTE");
    private boolean mute = false;

    public NwdConsoleImpl(Context context, final ConsoleProtoclListener listener) {
        super(context, listener);

        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction(ACTION_BT_END_CALL);
        localIntentFilter.addAction(ACTION_BT_BEGIN_CALL_ONLINE);
        localIntentFilter.addAction(ACTION_BT_INCOMING_CALL);
        localIntentFilter.addAction(ACTION_BT_OUTGOING_CALL);
        context.registerReceiver(callBroadcastReceiver, localIntentFilter);

        Log.d(TAG, "nwd console init");


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.nwd.action.ACTION_NO_SOURCE_DEVICE_CHANGE");
        context.registerReceiver(deviceStateBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver callBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_BT_BEGIN_CALL_ONLINE.equals(intent.getAction())) {
                listener.callState(true);
            } else if (ACTION_BT_INCOMING_CALL.equals(intent.getAction())) {
                listener.callState(true);
            } else if (ACTION_BT_OUTGOING_CALL.equals(intent.getAction())) {
                listener.callState(true);
            } else if (ACTION_BT_END_CALL.equals(intent.getAction())) {
                listener.callState(false);
            }
        }
    };


    private BroadcastReceiver deviceStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if ("com.nwd.action.ACTION_NO_SOURCE_DEVICE_CHANGE".equals(intent.getAction())) {
                if ((0x2 & getIntValue(context.getContentResolver(), NO_SOURCE_DEVICE_STATE)) == 2) {
                    listener.lightState(true);
                } else {
                    listener.lightState(false);
                }
            }
        }
    };

    @Override
    public void decVolume() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 15);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public void incVolume() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 14);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public synchronized void mute() {
        if (mute) {
            this.mMuteIntent.putExtra("extra_mute", false);
            this.context.sendBroadcast(this.mMuteIntent);
            mute = false;
        } else {
            this.mMuteIntent.putExtra("extra_mute", true);
            this.context.sendBroadcast(this.mMuteIntent);
            mute = true;
        }
    }

    @Override
    public void clearTask() {
        ToastManage.self().show("不支持的指令");
    }

    @Override
    public void callAnswer() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 21);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public void callHangup() {
        this.mSetVolumeIntent.putExtra("extra_key_value", (byte) 22);
        this.context.sendBroadcast(this.mSetVolumeIntent);
    }

    @Override
    public void destroy() {
        context.unregisterReceiver(callBroadcastReceiver);
        context.unregisterReceiver(deviceStateBroadcastReceiver);

        Log.d(TAG, "nwd console destroy");
    }


    public static final int getIntValue(ContentResolver paramContentResolver, String paramString) {
        try {
            int i = Settings.System.getInt(paramContentResolver, paramString);
            return i;
        } catch (Settings.SettingNotFoundException localSettingNotFoundException) {
        }
        return 0;
    }

    public static final String getStringValue(ContentResolver paramContentResolver, String paramString) {
        try {
            String str = Settings.System.getString(paramContentResolver, paramString);
            return str;
        } catch (Exception localException) {
        }
        return null;
    }

    public static final void writeDataToTable(ContentResolver paramContentResolver, String paramString, int paramInt) {
        boolean bool = Settings.System.putInt(paramContentResolver, paramString, paramInt);
    }

    public static final void writeDataToTable(ContentResolver paramContentResolver, String paramString1, String paramString2) {
        boolean bool = Settings.System.putString(paramContentResolver, paramString1, paramString2);
    }

    public static abstract interface AudioSettingTable {
        public static final String AUDIO_STATE = "mcu_audio_state";
        public static final String BALANCE = "mcu_balance";
        public static final String BASS = "mcu_bass";
        public static final String BEE = "mcu_bee";
        public static final String BT_VOLUME = "mcu_bt_volume";
        public static final String CENTER_CHANNEL = "mcu_center_channel";
        public static final String DSP_CENTER = "mcu_dsp_center";
        public static final String DSP_DEFINITION = "mcu_dsp_definition";
        public static final String DSP_FOCUS = "mcu_dsp_focus";
        public static final String DSP_MAX_CENTER = "mcu_dsp_max_center";
        public static final String DSP_MAX_DEFINITION = "mcu_dsp_max_definition";
        public static final String DSP_MAX_FOCUS = "mcu_dsp_max_focus";
        public static final String DSP_MAX_SPACE = "mcu_dsp_max_space";
        public static final String DSP_MAX_SPEAKER = "mcu_dsp_max_speaker";
        public static final String DSP_MAX_TRUBASS = "mcu_dsp_max_trubass";
        public static final String DSP_SPACE = "mcu_dsp_space";
        public static final String DSP_SPEAKER = "mcu_dsp_speaker";
        public static final String DSP_SRS = "mcu_dsp_srs";
        public static final String DSP_TRUBASS = "mcu_dsp_trubass";
        public static final String EQTYPE = "mcu_eq";
        public static final String EQUAL = "mcu_state_equal";
        public static final String FADER = "mcu_fader";
        public static final String GAIN_AUX = "mcu_gain_aux";
        public static final String GAIN_AUX2 = "mcu_gain_aux2";
        public static final String GAIN_BT = "mcu_gain_bt";
        public static final String GAIN_CANBUS_USB = "mcu_gain_canbus_usb";
        public static final String GAIN_CDC = "mcu_gain_cdc";
        public static final String GAIN_DVD = "mcu_gain_dvd";
        public static final String GAIN_GPS = "mcu_gain_gps";
        public static final String GAIN_IPOD = "mcu_gain_ipod";
        public static final String GAIN_MP5 = "mcu_gain_mp5";
        public static final String GAIN_RADIO = "mcu_gain_radio";
        public static final String GAIN_TV = "mcu_gain_tv";
        public static final String GPS_VOLUME = "mcu_gps_volume";
        public static final String HEAVY_BASS = "mcu_heavy_bass";
        public static final String LAST_SYSTEM_VOLUME = "mcu_last_mute_system_volume";
        public static final String MAX_BALANCE_FADER = "mcu_max_balance_fader";
        public static final String MAX_BEE = "mcu_max_bee";
        public static final String MAX_GAIN_VOLUME = "mcu_max_gain_volume";
        public static final String MAX_HORN_VOLUME = "mcu_max_horn_volume";
        public static final String MAX_TREBLE_TENOR_BASS_VOLUME = "mcu_max_treble_tenor_bass_volume";
        public static final String MAX_VOLUME = "mcu_max_volume";
        public static final String MUTE_STATE = "mcu_mute_state";
        public static final String SYSTEM_VOLUME = "mcu_system_volume";
        public static final String TENOR = "mcu_tenor";
        public static final String TREBLE = "mcu_treble";
    }

    public static abstract interface CANBUSSettingTable {
        public static final String BACK_CAR_VIDEO_SWITCH = "can_back_car_video_switch";
        public static final String CANBUS_VERSION = "canbus_version";
        public static final String DOOR_SHOW_STATE = "can_door_show_state";
    }

    public static abstract interface DspSettingTable {
        public static final String DSP_3D_SURROUND_SWITCH = "dsp_3d_surround_switch";
        public static final String DSP_DC_BASS_FREQUENCY = "dsp_dc_bass_frequency";
        public static final String DSP_DC_BASS_FREQUENCY_STEP = "dsp_dc_bass_frequency_step";
        public static final String DSP_DC_BASS_GAIN = "dsp_dc_bass_gain";
        public static final String DSP_DC_BASS_GAIN_STEP = "dsp_dc_bass_gain_step";
        public static final String DSP_DC_HIGH_FREQUENCY_EXTENTION = "dsp_dc_high_frequency_extention";
        public static final String DSP_DC_HIGH_FREQUENCY_EXTENTION_STEP = "dsp_dc_high_frequency_extention_step";
        public static final String DSP_DC_LOW_FREQUENCY_COMPRESSION = "dsp_dc_low_frequency_compression";
        public static final String DSP_DC_LOW_FREQUENCY_COMPRESSION_STEP = "dsp_dc_low_frequency_compression_step";
        public static final String DSP_DC_SWITCH = "dsp_dc_switch";
        public static final String DSP_DC_TREBLE_FREQUENCY = "dsp_dc_treble_frequency";
        public static final String DSP_DC_TREBLE_FREQUENCY_STEP = "dsp_dc_treble_frequency_step";
        public static final String DSP_DC_TREBLE_GAIN = "dsp_dc_treble_gain";
        public static final String DSP_DC_TREBLE_GAIN_STEP = "dsp_dc_treble_gain_step";
        public static final String DSP_DC_TREBLE_STRENGTHEN_SWITCH = "dsp_dc_treble_strengthen_switch";
        public static final String DSP_EQ_11 = "dsp_eq_11";
        public static final String DSP_EQ_12 = "dsp_eq_12";
        public static final String DSP_EQ_125HZ = "dsp_eq_125hz";
        public static final String DSP_EQ_13 = "dsp_eq_13";
        public static final String DSP_EQ_16KHZ = "dsp_eq_16Khz";
        public static final String DSP_EQ_1KHZ = "dsp_eq_1khz";
        public static final String DSP_EQ_250HZ = "dsp_eq_250hz";
        public static final String DSP_EQ_2KHZ = "dsp_eq_2Khz";
        public static final String DSP_EQ_32HZ = "dsp_eq_32hz";
        public static final String DSP_EQ_4KHZ = "dsp_eq_4Khz";
        public static final String DSP_EQ_500HZ = "dsp_eq_500hz";
        public static final String DSP_EQ_64HZ = "dsp_eq_64hz";
        public static final String DSP_EQ_8KHZ = "dsp_eq_8Khz";
        public static final String DSP_HEAVYBASS_CUTOFF_FREQUENCY = "dsp_cutoff_frequency";
        public static final String DSP_HEAVYBASS_CUTOFF_FREQUENCY_STEP = "dsp_cutoff_frequency_step";
        public static final String DSP_HEAVYBASS_GAIN = "dsp_gain";
        public static final String DSP_HEAVYBASS_GAIN_STEP = "dsp_gain_step";
        public static final String DSP_HEAVYBASS_MAX_CUTOFF_FREQUENCY = "dsp_max_cutoff_frequency";
        public static final String DSP_HEAVYBASS_MAX_GAIN = "dsp_max_gain";
        public static final String DSP_HEAVYBASS_MAX_PHASE = "dsp_max_phase";
        public static final String DSP_HEAVYBASS_MIN_CUTOFF_FREQUENCY = "dsp_min_cutoff_frequency";
        public static final String DSP_HEAVYBASS_MIN_GAIN = "dsp_min_gain";
        public static final String DSP_HEAVYBASS_MIN_PHASE = "dsp_min_phase";
        public static final String DSP_HEAVYBASS_PHASE = "dsp_phase";
        public static final String DSP_HEAVYBASS_PHASE_STEP = "dsp_phase_step";
        public static final String DSP_HEAVYBASS_SWITCH = "dsp_heavy_bass_switch";
        public static final String DSP_LOUDNESS_GAIN = "dsp_loudness_gain";
        public static final String DSP_LOUDNESS_GAIN_STEP = "dsp_loudness_step";
        public static final String DSP_LOUDNESS_SWITCH = "dsp_loudness_switch";
        public static final String DSP_MAX_DC_BASS_FREQUENCY = "dsp_max_dc_bass_frequency";
        public static final String DSP_MAX_DC_BASS_GAIN = "dsp_max_dc_bass_gain";
        public static final String DSP_MAX_DC_HIGH_FREQUENCY_EXTENTION = "dsp_max_dc_high_frequency_extention";
        public static final String DSP_MAX_DC_LOW_FREQUENCY_COMPRESSION = "dsp_max_dc_low_frequency_compression";
        public static final String DSP_MAX_DC_TREBLE_FREQUENCY = "dsp_max_dc_treble_frequency";
        public static final String DSP_MAX_DC_TREBLE_GAIN = "dsp_max_dc_treble_gain";
        public static final String DSP_MAX_EQ = "dsp_max_eq";
        public static final String DSP_MAX_LOUDNESS_GAIN = "dsp_max_londness_gain";
        public static final String DSP_MAX_TREBLE_STRENGTHEN_GAIN = "dsp_max_treble_strengthen_gain";
        public static final String DSP_MIN_DC_BASS_FREQUENCY = "dsp_min_dc_bass_frequency";
        public static final String DSP_MIN_DC_BASS_GAIN = "dsp_min_dc_bass_gain";
        public static final String DSP_MIN_DC_HIGH_FREQUENCY_EXTENTION = "dsp_min_dc_high_frequency_extention";
        public static final String DSP_MIN_DC_LOW_FREQUENCY_COMPRESSION = "dsp_min_dc_low_frequency_compression";
        public static final String DSP_MIN_DC_TREBLE_FREQUENCY = "dsp_min_dc_treble_frequency";
        public static final String DSP_MIN_DC_TREBLE_GAIN = "dsp_min_dc_treble_gain";
        public static final String DSP_MIN_LOUDNESS_GAIN = "dsp_min_loudness_gain";
        public static final String DSP_MIN_TREBLE_STRENGTHEN_GAIN = "dsp_min_treble_strengthen_gain";
        public static final String DSP_TREBLE_STRENGTHEN = "dsp_treble_strengthen";
        public static final String DSP_TREBLE_STRENGTHEN_STEP = "dsp_treble_strengthen_step";
    }

    public static abstract interface OsSettingTable {
        public static final String GSENSOR_FEATURE_ENABLE = "nwd_gsensor_feature_enable";
        public static final String HARDWARE_VERSION = "nwd_hardware_version";
        public static final String HAVE_IPOD = "nwd_have_ipod";
    }

    public static abstract interface PublicSettingTable {
        public static final String ARM_DEVICE_STATE = "arm_device_state";
        public static final String ARM_VOLUME = "arm_volume";
        public static final String ARM_VOLUME_TYPE = "nwd_arm_volume_type";
        public static final String AUX_STATE = "mcu_aux_state";
        public static final String BACKCAR_STATE = "mcu_backcar_state";
        public static final String BT_PHONE_STATUS = "bt_phone_status";
        public static final String BT_VERSION = "nwd_bt_version";
        public static final String CAN_VERSION = "can_version";
        public static final String CURRENT_CONTROLLER = "mcu_current_controller";
        public static final String CURRENT_SOURCE = "mcu_current_source";
        public static final String CURRENT_TOUCU = "mcu_current_touch";
        public static final String CVBS_OUT_TYPE = "nwd_cvbs_out_type";
        public static final String DEVICE_STATE = "mcu_device_state";
        public static final String DISABLE_REPORT_IMCOMMING_CALL_INFO = "nwd_disable_report_imcomming_call_info";
        public static final String DVD_VERSION = "dvd_version";
        public static final String FEATURE_DATA_0 = "mcu_feature_data_0";
        public static final String FEATURE_DATA_1 = "mcu_feature_data_1";
        public static final String FEATURE_DATA_2 = "mcu_feature_data_2";
        public static final String FEATURE_DATA_3 = "mcu_feature_data_3";
        public static final String FEATURE_DATA_4 = "mcu_feature_data_4";
        public static final String FEATURE_DATA_5 = "mcu_feature_data_5";
        public static final String FEATURE_DATA_6 = "mcu_feature_data_6";
        public static final String FEATURE_DATA_7 = "mcu_feature_data_7";
        public static final String GPS_APP_NAME = "navigation_appname";
        public static final String GPS_CLASS_NAME = "navigation_classname";
        public static final String GPS_PACKAGE_NAME = "navigation_packagename";
        public static final String GPS_SYNC_TIME_SWITCH = "gps_sync_time_switch";
        public static final String HAND_BREAK = "mcu_hand_break";
        public static final String IS_ALLOW_MCU_SWITCH_VIDEO = "is_allow_mcu_switch_video";
        public static final String IS_NEEDED_SHOW_TOASTPANEL = "show_toast_panel";
        public static final String LANGUAGE_DATA_1 = "mcu_language_data_1";
        public static final String LIGHT_STATE = "mcu_light_state";
        public static final String LOCK_SCREEN_STATE = "lock_screen_state";
        public static final String MCU_MHL_VIDEO_STATE = "mcu_mhl_video_state";
        public static final String MCU_STATE = "mcu_state";
        public static final String MCU_VERSION = "mcu_version";
        public static final String MCU_VIDEO_STATE = "mcu_video_state";
        public static final String NO_SOURCE_DEVICE_STATE = "mcu_no_source_device_state";
        public static final String PARKING_RADAR_STATE = "can_parking_radar_state";
        public static final String SETTING_BRAKE_CHECK = "os_setting_brake_check";
        public static final String SETTING_KEY_BEE = "os_setting_key_bee";
        public static final String SETTING_LIGHT_CHECK = "os_setting_light_check";
        public static final String SYSTEM_UI_VISIBILITY = "system_ui_visibility";
        public static final String VIDEO_SIGNAL = "mcu_video_signal";
        public static final String WAKEUP_VOICE_ANALYZE_SWITCH = "nwd_voice_analyze_switch";
    }

    public static abstract interface SystemSettingTable {
        public static final String AMPLIFIER_SWITCH = "mcu_amplifier_switch";
        public static final String BACKLIGTH_DAY = "mcu_back_light_day";
        public static final String BACKLIGTH_MAX = "mcu_back_light_max";
        public static final String BACKLIGTH_NIGHT = "mcu_back_light_night";
        public static final String BACKLIGTH_STATE = "mcu_back_light_state";
        public static final String BACK_CAR_STATE = "arm_back_car_state";
        public static final String BEE_SWITCH = "arm_bee_switch";
        public static final String HAND_BREAK_STATE = "arm_hand_break_state";
        public static final String OTHER_SETTING_AVT_TYPE = "mcu_other_setting_atv_type";
        public static final String OTHER_SETTING_DEFAULT_VOLUME = "mcu_other_setting_default_volume";
        public static final String OTHER_SETTING_GPS_VOLUME_TYPE = "mcu_other_setting_gps_setting_type";
        public static final String OTHER_SETTING_VOLUME_MIX = "mcu_other_setting_volume_mix";
        public static final String RADIO_AREA_CURRENT = "mcu_radio_area_current";
        public static final String RADIO_AREA_FEATURE = "mcu_radio_area_feature";
        public static final String SMART_FAN_SWITCH = "mcu_smart_fan_switch";
        public static final String STEERING_WHEEL_STATE = "mcu_steering_wheel_state";
        public static final String SYSTEM_STATE = "mcu_System_setting_state";
        public static final String TIME_SYNC = "mcu_time_sync";
        public static final String TRICOLOR_BLUE = "mcu_tricolor_blue";
        public static final String TRICOLOR_GREEN = "mcu_tricolor_green";
        public static final String TRICOLOR_RED = "mcu_tricolor_red";
        public static final String VIDEO_BRIGTHNESS = "mcu_video_setting_brigthness";
        public static final String VIDEO_COLOR = "mcu_video_setting_color";
        public static final String VIDEO_CONTRAST = "mcu_video_setting_contrast";
        public static final String VIDEO_MAX = "mcu_video_setting_max";
        public static final String VIDEO_MODE = "mcu_video_setting_mode";
    }
}
