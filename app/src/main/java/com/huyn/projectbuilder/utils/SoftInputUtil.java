package com.huyn.projectbuilder.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

/**
 * @description 软键盘隐藏显示工具集
 */
public class SoftInputUtil {

    /**
     * @description 如果输入法在窗口上已经显示，则隐藏，反之则显示
     */
    public static void showOrHide(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * @param view 为接受软键盘输入的视图
     * @description SHOW_FORCED表示强制显示
     */
    public static void showSoftInput(View view) {
        if (view == null)
            return;
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * @description Activity隐藏系统默认的输入法
     */
    public static void hideSysSoftInput(Activity activity) {
        View focusView = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (focusView != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @return 若返回true，则表示输入法打开
     * @description 获取输入法打开的状态
     */
    public static boolean isOpen(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * @param view
     * @description 强制隐藏键盘
     */
    public static void hideShow(View view) {
        if (view == null)
            return;
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);// 强制隐藏键盘
    }

    /**
     * @param view 一般是EditText
     *             一般控件xml需要    android:focusable="false"
     *             android:focusableInTouchMode="false"
     * @description 点击该控件重新获取焦点并打开键盘（配合hideShow使用）
     */
    public static void requstShow(final View view) {
        if (view == null)
            return;
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.setOnClickListener(new OnClickListener() {// 打开键盘
            @Override
            public void onClick(View v) {
                // view.clearFocus(); //失去焦点
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        });
    }

}
