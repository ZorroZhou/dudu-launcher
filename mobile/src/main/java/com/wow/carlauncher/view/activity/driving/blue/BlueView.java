package com.wow.carlauncher.view.activity.driving.blue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.TaskExecutor;
import com.wow.carlauncher.ex.plugin.obd.evnet.PObdEventCarInfo;
import com.wow.carlauncher.view.activity.driving.DrivingView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

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


    private boolean show = true;

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
                        iv_vss_cursor.setVisibility(VISIBLE);
                        iv_fuel_cursor.setVisibility(VISIBLE);
                        iv_fuel_mask.setVisibility(VISIBLE);
                        iv_rpm_mask.setVisibility(VISIBLE);
                        iv_rpm_cursor.setVisibility(VISIBLE);
                        tv_speed.setVisibility(VISIBLE);

                        iv_rpm_mask.setRotation((((float) 0 / (float) MAX_REV) * 87 - 65));
                        ll_right.setVisibility(VISIBLE);
                    }
                });
            }
        }, 1000);
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
