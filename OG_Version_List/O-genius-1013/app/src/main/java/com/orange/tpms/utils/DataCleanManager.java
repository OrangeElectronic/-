package com.orange.tpms.utils;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class DataCleanManager {
    private static DataOutputStream dos = null;
    private static Process p = null;

    /**
     * 保留函式
     * 執行root許可權下的命令
     * @param cmd 命令
     */
    private static void runRootCmd(String cmd){
        try {
            p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.close();
            p.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(dos != null){
                try{
                    dos.close();
                }catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    /**
     * 刪除輸入引數目錄下的檔案
     * @param directory 檔案目錄
     */
    private static void deleteFileByDirectory(File directory){
        if(directory != null && directory.exists() && directory.isDirectory()){
            for(File file : directory.listFiles() )
                file.delete();
        }
    }

    /**
     * 刪除目錄下的指定檔名的檔案
     * @param directory 檔案目錄
     * @param filename 檔名
     */
    private static void deleteFileByDirectory(File directory, String filename){
        if(directory != null && directory.exists() && directory.isDirectory()){
            for(File file : directory.listFiles()){
                if(file.toString().equals(filename))
                    file.delete();
            }
        }
    }

    /**
     * 清除快取檔案
     * @param context
     */
    private void cleanInternalCache(Context context){
        deleteFileByDirectory(context.getCacheDir());
    }

    /**
     * 清除database資料夾下的webview.db和 webviewCache.db
     * @param context
     */
    private void cleanDatabases(Context context){
        String dataBasesFilePath = "/data/data" + context.getPackageName() + "/databases";
        deleteFileByDirectory(new File(dataBasesFilePath), "webview.db");
        deleteFileByDirectory(new File(dataBasesFilePath), "webviewCache.db");
    }

    /**
     * 清除webView相關的快取
     * @param context
     */
    public void cleanCache(Context context){
        cleanInternalCache(context);
        cleanDatabases(context);
    }

}
