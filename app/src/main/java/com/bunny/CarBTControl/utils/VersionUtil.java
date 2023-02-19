package com.bunny.CarBTControl.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Project:  51CarBlueToothControl
 * Comments: 安卓版本权限处理工具类（针对安卓12蓝牙权限的动态申请）
 * JDK version used: <JDK1.8>
 * Create Date：2023-01-05
 * Version: 1.0
 */

public class VersionUtil {
    private final Context context;

    public VersionUtil(Context context){
        this.context = context;
    }

    public boolean isAndroid12(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            return true;
        }
        return false;
    }

    public boolean hasPermission(String permission){
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this.context, permission)){
            return true;
        }
        return false;
    }
}
