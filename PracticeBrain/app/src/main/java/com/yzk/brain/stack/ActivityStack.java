package com.yzk.brain.stack;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 11/10/16.
 */

public class ActivityStack {
    public static List<Activity> mActivityList = new ArrayList<Activity>();

    public static void remove(Activity activity) {
        if (mActivityList == null) {
            return;
        }

        mActivityList.remove(activity);
    }

    public static void add(Activity activity) {
        if (mActivityList == null) {
            return;
        }

        mActivityList.add(activity);
    }

    public static void finishProgram() {
        if (mActivityList == null) {
            return;
        }

        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }

    public static boolean isActivityRunning(String className) {
        if (className == null || mActivityList == null) {
            return false;
        }

        for (Activity activity : mActivityList) {
            if (activity.getClass().getName().equals(className)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取最顶端Activity
     */
    public static Activity getTopActivity() {
        if (mActivityList != null && mActivityList.size() > 0) {
            return mActivityList.get(mActivityList.size() - 1);
        }
        return null;
    }

    /**
     * 根据类名获取Activity
     */
    public static Activity getActivity(String className) {
        if (className != null && !isActivityStackEmpty()) {
            Activity activity;
            for (int i = mActivityList.size() - 1; i >= 0; i--) {
                activity = mActivityList.get(i);
                if (activity.getClass().getName().equals(className)) {
                    return activity;
                }
            }
        }

        return null;
    }

    /**
     * 获取最顶的上一个Activity
     */
    public static Activity getTopPreviousActivity() {
        if (mActivityList != null && mActivityList.size() > 1) {
            return mActivityList.get(mActivityList.size() - 2);
        }
        return null;
    }

    /**
     * 判断Activity是否运行在栈顶
     */
    public static boolean isActivityOnTop(String className) {
        Activity activity = getTopActivity();
        if (activity != null) {
            return activity.getClass().getName().equals(className);
        }
        return false;
    }

    /**
     * 判断Activity栈是否为空
     */
    public static boolean isActivityStackEmpty() {
        if (mActivityList == null) {
            return true;
        }

        return mActivityList.isEmpty();
    }

    public void finishAllActivity() {
        for (int i = 0, size = mActivityList.size(); i < size; i++) {
            if (null != mActivityList.get(i)) {
                mActivityList.get(i).finish();
            }
        }
        mActivityList.clear();
    }



    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {

        }
    }
}
