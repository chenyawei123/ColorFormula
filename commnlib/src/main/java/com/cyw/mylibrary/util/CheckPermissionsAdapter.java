package com.cyw.mylibrary.util;

import android.app.Activity;
import android.content.DialogInterface;

import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;


public abstract class CheckPermissionsAdapter implements CheckRequestPermissionsListener {

    @Override
    public void onPermissionDenied(Permission[] refusedPermissions) {
        //SoulPermission提供栈顶Activity
        Activity activity = SoulPermission.getInstance().getTopActivity();
        if (null == activity) {
            return;
        }
        String permissionDesc = "";
        for (int i = 0; i < refusedPermissions.length; i++) {
            permissionDesc += refusedPermissions[i].getPermissionNameDesc();
        }
        new android.app.AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //去设置页
                        SoulPermission.getInstance().goPermissionSettings();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
