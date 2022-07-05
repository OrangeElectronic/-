package com.orango.electronic.orange_og_lib.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
/**
 * Home鍵監聽封裝
 *
 * @author way
 *
 */
public class HomeWatcher {
    // 回撥介面
    public interface OnHomePressedListener {
        void onHomePressed();
        void onHomeLongPressed();
    }
    private static final String TAG = "HomeWatcher";
    /** 上下文 */
    private Context mContext;
    /** 過濾器 */
    private IntentFilter mFilter;
    /** 介面 */
    private OnHomePressedListener mListener;
    /** 廣播接收者 */
    private InnerRecevier mRecevier;
    public HomeWatcher(Context context) {
        mContext = context;
        mRecevier = new InnerRecevier();
        mFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }
    /**
     * 設定監聽
     *
     * @param listener
     */
    public void setOnHomePressedListener(OnHomePressedListener listener) {
        mListener = listener;
    }
    /**
     * 開始監聽，註冊廣播
     */
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, mFilter);
        }
    }
    /**
     * 停止監聽，登出廣播
     */
    public void stopWatch() {
        try{
            if (mRecevier != null) {
                mContext.unregisterReceiver(mRecevier);
            }
        }catch (Exception e){e.printStackTrace();}

    }
    /**
     * 廣播接收者
     */
    private class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    Log.i(TAG, "action:" + action + ",reason:" + reason);
                    if (mListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
// 短按home鍵
                            mListener.onHomePressed();
                        } else if (reason
                                .equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
// 長按home鍵
                            mListener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }
}