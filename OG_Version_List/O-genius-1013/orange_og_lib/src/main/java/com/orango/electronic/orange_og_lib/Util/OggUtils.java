package com.orango.electronic.orange_og_lib.Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 常用工具类
 */
public class OggUtils {
    public static long  byteArrayToInt(byte[] b) {
        long a= (b[3] & (long)0xFF |
                (b[2] & (long)0xFF) << 8 |
                (b[1] & (long)0xFF) << 16 |
                (b[0] & (long)0xFF) << 24);
        return a;

    }
    /**
     * 判断空间内容是否为空
     * @param editText View
     * @return boolean empty
     */
    public static boolean isEmpty(EditText editText){
        if(editText != null){
            String content = editText.getText().toString().trim().replace(" ","");
            return content.isEmpty();
        }
        return false;
    }

    /**
     * 判断空间内容是否为空
     * @param textView View
     * @return boolean empty
     */
    public static boolean isEmpty(TextView textView){
        if(textView != null){
            String content = textView.getText().toString().trim().replace(" ","");
            return content.isEmpty();
        }
        return false;
    }

    /**
     * 获取输入框内容并去除所有空格
     *
     * @param ed 输入框
     * @return 字符 ed
     */
    public static String getEd(EditText ed) {
        return ed.getText().toString().trim().replace(" ", "");
    }

    /**
     * 获取输入框内容并去除所有空格
     *
     * @param tx 输入框
     * @return 字符 tx
     */
    public static String getTx(TextView tx) {
        return tx.getText().toString().trim();
    }

    /**
     * 隐藏软键盘
     *
     * @param activity the activity
     */
    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    public static void hide_keyboard_from(EditText view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
