
package com.hjq.demo.ui.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.hjq.demo.R;
import com.hjq.demo.app.AppApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


@TargetApi(Build.VERSION_CODES.KITKAT)
public class UserTitleBarUtil {
    private static final String TAG = "WpkTitleBarUtil";
    public static boolean TRANSLUCENT_STATUS_ENABLED = false;


    @SuppressLint("NewApi")
    public static void enableTranslucentStatus(Window window) {
        if (window == null) return;
        TRANSLUCENT_STATUS_ENABLED = translucent(window, true);
    }

    @SuppressLint("NewApi")
    public static void enableWhiteTranslucentStatus(Window window) {
        if (window == null) return;
        TRANSLUCENT_STATUS_ENABLED = translucent(window, false);
    }

    public static void setTitleBarPadding(View titleBar) {
        if (titleBar == null) return;
        int topPadding = getStatusHeight();
        setTitleBarPadding(topPadding, titleBar);
    }

    public static void setTitleBarPadding(int topPadding, View titleBar) {
        if (!TRANSLUCENT_STATUS_ENABLED || titleBar == null) return;

        if (titleBar.getLayoutParams().height >= 0)
            titleBar.getLayoutParams().height += topPadding;
        titleBar.setPadding(0, topPadding, 0, 0);
        titleBar.setLayoutParams(titleBar.getLayoutParams());
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean translucent(Window window, boolean isDarkText) {
        if (Build.BRAND.equalsIgnoreCase("htc") && Build.MODEL.contains("M8w")) {
            return false;
        }
        try {

            if (setMIUIStatusBarLightMode(window, isDarkText)) {
                if (isDarkText) {
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                } else {
                    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    View view = window.getDecorView();
                    window.getDecorView().setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

                }
            } else if (setFlymeStatusBarLightMode(window, isDarkText)) {
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isDarkText) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                String brand = Build.BRAND;
                Log.i("StatusBar", "Build.BRAND = " + brand);
                if (isDarkText && (Build.BRAND.equalsIgnoreCase("oppo") || Build.BRAND.equalsIgnoreCase("vivo") || Build.BRAND.equalsIgnoreCase("nubia"))) {
                    window.setStatusBarColor(0x01ffffff);
                } else {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }
        return true;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    @SuppressWarnings("java:S3011")
    private static boolean setFlymeStatusBarLightMode(Window window, boolean dark) {
        if (window == null) return false;
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field flags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            flags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = flags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            flags.setInt(lp, value);
            window.setAttributes(lp);
            result = true;
        } catch (Exception ignored) {
            //
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setMIUIStatusBarLightMode(Window window, boolean dark) {
        if (window == null) return false;
        boolean result = false;
        Class<?> clazz = window.getClass();
        try {
            int tranceFlag;
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);//状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, tranceFlag, tranceFlag | darkModeFlag);//清除黑色字体
            }
            result = true;
        } catch (Exception ignored) {
        }
        return result;
    }

    public static int getStatusHeight() {
        int statusHeight = -1;
        try {
            Resources resources = AppApplication.sInstance.getResources();
            statusHeight = resources.getDimensionPixelOffset(resources.getIdentifier("status_bar_height", "dimen", "android"));
        } catch (Exception e) {
            // ignore.
        }
        return statusHeight;
    }
    //////////////////////


    public static void setTitleBar(Activity activity, View rootView) {
//        if (!TRANSLUCENT_STATUS_ENABLED || activity == null) {
//            return;
//        }
//        View titleBar;
//        if (rootView == null) {
//            titleBar = activity.findViewById(R.id.title_bar);
//        } else {
//            titleBar = rootView.findViewById(R.id.title_bar);
//        }
//
//        if (titleBar == null)
//            return;
//        if (TOP_PADDING == -1) {
//            int topPadding = activity.getResources().getDimensionPixelSize(
//                    R.dimen.title_bar_top_padding);
//            try {
//                Resources resources = AppApplication.sInstance.getResources();
//                topPadding = resources.getDimensionPixelOffset(
//                        resources.getIdentifier("status_bar_height", "dimen", "android"));
//            } catch (Exception e) {
//                // ignore.
//            }
//            TOP_PADDING = topPadding;
//        }
//        if (titleBar.getLayoutParams().height > 0)
//            titleBar.getLayoutParams().height += TOP_PADDING;
//        titleBar.setPadding(0, TOP_PADDING, 0, 0);
//        titleBar.setLayoutParams(titleBar.getLayoutParams());

    }

    private static int TOP_PADDING = -1;



    @SuppressLint("NewApi")
    public static void enableTranslucentStatus(Activity activity) {
        if (activity == null) return;
        TRANSLUCENT_STATUS_ENABLED = translucent(activity.getWindow(), true);
    }

    @SuppressLint("NewApi")
    public static void enableWhiteTranslucentStatus(Activity activity) {
        if (activity == null) return;
        TRANSLUCENT_STATUS_ENABLED = translucent(activity.getWindow(), false);
    }


    public static void setTitleBar(Activity activity) {
        setTitleBar(activity, null);
    }


    /**
     * [沉浸状态栏]
     */
    @SuppressWarnings("unused")
    private void steepStatusBar(Activity activity) {
        // 透明状态栏
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

}
