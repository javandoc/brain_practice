package com.yzk.brain.base;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yzk.brain.R;
import com.yzk.brain.application.ApplicationController;
import com.yzk.brain.stack.ActivityStack;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import xiaofei.library.hermeseventbus.HermesEventBus;


/**
 * base activity
 */

public abstract class BaseFragmentActivity extends FragmentActivity {
    protected Handler mHandler = new Handler();
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.add(this);
//        EventBus.getDefault().register(this);
        HermesEventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                   );
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            int greenColor = getResources().getColor(R.color.yellow_ffd56d);
            tintManager.setTintColor(greenColor);
        }

        activityCreate();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parentView.setFitsSystemWindows(true);
        }
        try {
            uIViewInit();
        } catch (Exception e) {

        }
        uIViewDataApply();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        onWindowHasFocus(hasFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.remove(this);
        HermesEventBus.getDefault().unregister(this);
        HermesEventBus.getDefault().destroy();
        activityDestroy();
        ButterKnife.unbind(this);
    }

    public void onLeftClick() {
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onLeftClick();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * activity view 初始化
     */
    protected abstract void uIViewInit();

    /**
     * activity view 初始化完成调用
     */
    protected abstract void uIViewDataApply();

    /**
     * activity 创建
     */
    protected void activityCreate() {
    }

    ;

    /**
     * activity 销毁
     */
    protected void activityDestroy() {
        ApplicationController.getInstance().cancelPendingRequests(this);
    }

    ;

    /**
     * window 是否获取焦点
     */

    public void onWindowHasFocus(boolean hasFocus) {
    }

    ;

    @Subscribe
    public void onEvent(String object) {
    }

    /**
     * 将list顺序打乱
     *
     * @param sourceList
     * @return
     */
    protected ArrayList randomList(ArrayList sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }
        ArrayList randomList = new ArrayList(sourceList.size());
        do {
            int randomIndex = Math.abs(new Random().nextInt(sourceList.size()));
            randomList.add(sourceList.remove(randomIndex));
        } while (sourceList.size() > 0);
        return randomList;
    }

    private boolean isEmpty(ArrayList sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

}
