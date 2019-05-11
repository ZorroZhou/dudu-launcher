package com.wow.carlauncher.view.activity.driving.blue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.common.util.CommonUtil;
import com.wow.carlauncher.common.util.DateUtil;
import com.wow.carlauncher.ex.manage.ImageManage;
import com.wow.carlauncher.ex.manage.location.event.MNewLocationEvent;
import com.wow.carlauncher.ex.manage.time.event.MTimeSecondEvent;
import com.wow.carlauncher.ex.plugin.music.MusicPlugin;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventCoverRefresh;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventInfo;
import com.wow.carlauncher.ex.plugin.music.event.PMusicEventState;
import com.wow.carlauncher.ex.plugin.obd.ObdPlugin;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarTp;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventConnect;
import com.wow.carlauncher.view.activity.driving.DrivingView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class BlueView extends DrivingView {
    private final static int MAX_REV = 8000;
    private final static int MAX_SPEED = 200;

    public BlueView(@NonNull Context context) {
        super(context);
    }

    public BlueView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setFront(boolean front) {

    }

    @Override
    protected int getContent() {
        return R.layout.content_driving_blue;
    }

    @BindView(R.id.iv_center)
    ImageView iv_center;

    @BindView(R.id.iv_vss_cursor)
    ImageView iv_vss_cursor;

    @BindView(R.id.iv_fuel_cursor)
    ImageView iv_fuel_cursor;

    @BindView(R.id.iv_fuel_mask)
    ImageView iv_fuel_mask;

    @BindView(R.id.iv_rpm_mask)
    ImageView iv_rpm_mask;

    @BindView(R.id.iv_rpm_cursor)
    ImageView iv_rpm_cursor;

    @BindView(R.id.tv_speed)
    TextView tv_speed;

    @BindView(R.id.ll_right)
    LinearLayout ll_right;


    @BindView(R.id.tv_tp_lf)
    TextView tv_tp_lf;

    @BindView(R.id.tv_tp_lb)
    TextView tv_tp_lb;

    @BindView(R.id.tv_tp_rf)
    TextView tv_tp_rf;

    @BindView(R.id.tv_tp_rb)
    TextView tv_tp_rb;

    @BindView(R.id.music_iv_play)
    ImageView music_iv_play;

    @BindView(R.id.tv_music_title)
    TextView tv_music_title;

    @BindView(R.id.tv_shui_temp)
    TextView tv_shui_temp;

    @BindView(R.id.tv_shengyu_oil)
    TextView tv_shengyu_oil;

    @BindView(R.id.tv_date_time)
    TextView tv_date_time;

    @BindView(R.id.tv_date_day)
    TextView tv_date_day;

    @BindView(R.id.rl_item1_not_nav_tp)
    View rl_item1_not_nav_tp;

    @BindView(R.id.rl_item1_not_nav_music)
    View rl_item1_not_nav_music;

    @BindView(R.id.music_iv_cover)
    ImageView music_iv_cover;

    @BindView(R.id.ll_obd_not_connect)
    View ll_obd_not_connect;

    @BindView(R.id.ll_obd_connect)
    View ll_obd_connect;

    private boolean show = true;

    private boolean obdConnect = false;

    private boolean loaded = false;

    @Override
    protected void initView() {
        super.initView();
        int max = 49;
        int start = 1;
        TaskExecutor.self().run(() -> {
            for (int i = start; i <= max; i++) {
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int index = i;
                post(() -> {
                    iv_center.setImageResource(getContext().getResources().getIdentifier("driving_blue_center_gif_" + index, "mipmap", getContext().getPackageName()));
                    if (index == max) {
                        loadOk();
                    }
                });
            }
        }, 1000);
    }

    @OnClick(value = {R.id.music_ll_prew, R.id.music_ll_next, R.id.music_ll_play})
    public void clickEvent(View view) {
        switch (view.getId()) {
            case R.id.music_ll_prew: {
                MusicPlugin.self().pre();
                break;
            }
            case R.id.music_ll_play: {
                MusicPlugin.self().playOrPause();
                break;
            }
            case R.id.music_ll_next: {
                MusicPlugin.self().next();
                break;
            }
        }
    }

    private void loadOk() {
        loaded = true;
        iv_vss_cursor.setVisibility(VISIBLE);
        iv_fuel_cursor.setVisibility(VISIBLE);
        iv_fuel_mask.setVisibility(VISIBLE);
        iv_rpm_mask.setVisibility(VISIBLE);
        iv_rpm_cursor.setVisibility(VISIBLE);
        tv_speed.setVisibility(VISIBLE);

        iv_rpm_mask.setRotation((((float) 0 / (float) MAX_REV) * 87 - 65));
        iv_fuel_mask.setRotation((((float) 0 / (float) MAX_REV) * 87 + 65));
        ll_right.setVisibility(VISIBLE);

        //同步一下信息
        onEvent(new PObdEventConnect().setConnected(ObdPlugin.self().isConnect()));
        onEvent(ObdPlugin.self().getCurrentPObdEventCarInfo());
        onEvent(ObdPlugin.self().getCurrentPObdEventCarTp());
        MusicPlugin.self().requestLast();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final MTimeSecondEvent event) {
        if (!loaded) {
            return;
        }
        Date d = new Date();
        String date = DateUtil.dateToString(d, "MM月 dd日 " + DateUtil.getWeekOfDate(d));
        String time = DateUtil.dateToString(d, "HH:mm:ss");
        if (time.startsWith("0")) {
            time = time.substring(1);
        }
        if (date.startsWith("0")) {
            date = date.substring(1);
        }
        this.tv_date_day.setText(date);
        this.tv_date_time.setText(time);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventInfo event) {
        if (!loaded) {
            return;
        }
        if (tv_music_title != null) {
            if (CommonUtil.isNotNull(event.getTitle())) {
                String msg = event.getTitle();
                if (CommonUtil.isNotNull(event.getArtist())) {
                    msg = msg + "-" + event.getArtist();
                }
                tv_music_title.setText(msg);
            } else {
                tv_music_title.setText("音乐名称");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventState event) {
        if (!loaded) {
            return;
        }
        if (music_iv_play != null) {
            if (event.isRun()) {
                music_iv_play.setImageResource(R.mipmap.ic_pause2_b);
            } else {
                music_iv_play.setImageResource(R.mipmap.ic_play2_b);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventConnect event) {
        if (!loaded) {
            return;
        }
        boolean showTp = false;
        obdConnect = event.isConnected();
        if (event.isConnected()) {
            if (ObdPlugin.self().supportTp()) {
                rl_item1_not_nav_tp.setVisibility(VISIBLE);
                rl_item1_not_nav_music.setVisibility(GONE);
                showTp = true;
            }
            ll_obd_connect.setVisibility(VISIBLE);
            ll_obd_not_connect.setVisibility(GONE);
        } else {
            ll_obd_connect.setVisibility(GONE);
            ll_obd_not_connect.setVisibility(VISIBLE);
        }
        if (!showTp) {
            rl_item1_not_nav_tp.setVisibility(GONE);
            rl_item1_not_nav_music.setVisibility(VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(MNewLocationEvent event) {
        if (!loaded) {
            return;
        }
        if (!obdConnect) {
            setSpeed((int) (event.getSpeed() * 60 * 60 / 1000));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarInfo event) {
        if (!loaded) {
            return;
        }
        if (event.getRev() != null) {
            setRev(event.getRev());
        }
        if (event.getSpeed() != null) {
            setSpeed(event.getSpeed());
        }
        if (event.getWaterTemp() != null) {
            String msg = event.getWaterTemp() + "°";
            tv_shui_temp.setText(msg);
        }

        if (event.getOilConsumption() != null) {
            String msg = event.getOilConsumption() + "%";
            tv_shengyu_oil.setText(msg);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PMusicEventCoverRefresh event) {
        if (!loaded) {
            return;
        }
        if (music_iv_cover != null) {
            ImageManage.self().loadImage(event.getUrl(), music_iv_cover, R.mipmap.music_dlogo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PObdEventCarTp event) {
        if (!loaded) {
            return;
        }
        if (tv_tp_lf != null && event.getlFTirePressure() != null) {
            tv_tp_lf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlFTirePressure(), event.getlFTemp()));
        }

        if (tv_tp_lb != null && event.getlBTirePressure() != null) {
            tv_tp_lb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getlBTirePressure(), event.getlBTemp()));
        }

        if (tv_tp_rf != null && event.getrFTirePressure() != null) {
            tv_tp_rf.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrFTirePressure(), event.getrFTemp()));
        }

        if (tv_tp_rb != null && event.getrBTirePressure() != null) {
            tv_tp_rb.setText(getContext().getString(R.string.driving_cool_black_tp, event.getrBTirePressure(), event.getrBTemp()));
        }
    }


    public void setRev(int rev) {
        if (show) {
            //tv_rev.setText(rev + "");
            if (rev > MAX_REV) {
                rev = MAX_REV;
            } else if (rev < 0) {
                rev = 0;
            }
            tagerRev = rev;
            postRev();
        }
    }

    private int currentRev = 0;
    private int tagerRev = 0;

    private void postRev() {
        //转速变化的区间
        int revChange = 20;
        if (revChange + currentRev < tagerRev) {
            currentRev = currentRev + revChange;
            if (currentRev > tagerRev) {
                currentRev = tagerRev;
            }
            postDelayed(() -> {
                iv_rpm_mask.setRotation((((float) currentRev / (float) MAX_REV) * 87 - 65));
                postRev();
            }, 1);
        } else if (revChange + currentRev > tagerRev) {
            currentRev = currentRev - revChange;
            if (currentRev < tagerRev) {
                currentRev = tagerRev;
            }
            postDelayed(() -> {
                iv_rpm_mask.setRotation((((float) currentRev / (float) MAX_REV) * 87 - 65));
                postRev();
            }, 1);
        }
    }

    public void setSpeed(int speed) {
        if (show) {
            tv_speed.setText(speed + "");
            if (speed > MAX_SPEED) {
                speed = MAX_SPEED;
            } else if (speed < 0) {
                speed = 0;
            }
            tagertSpeed = speed;
            postSpeed();
        }
    }

    private int currentSpeed = 0;
    private int tagertSpeed = 0;

    private void postSpeed() {
        //转速变化的区间
        int speedChange = 1;
        if (speedChange + currentSpeed < tagertSpeed) {
            currentSpeed = currentSpeed + speedChange;
            if (currentSpeed > tagertSpeed) {
                currentSpeed = tagertSpeed;
            }
            postDelayed(() -> {
                iv_vss_cursor.setRotation((float) (currentSpeed * 180) / (float) MAX_SPEED);
                postSpeed();
            }, 1);
        } else if (speedChange + currentSpeed > tagertSpeed) {
            currentSpeed = currentSpeed - speedChange;
            if (currentSpeed < tagertSpeed) {
                currentSpeed = tagertSpeed;
            }
            postDelayed(() -> {
                iv_vss_cursor.setRotation((float) (currentSpeed * 180) / (float) MAX_SPEED);
                postSpeed();
            }, 1);
        }
    }
}
