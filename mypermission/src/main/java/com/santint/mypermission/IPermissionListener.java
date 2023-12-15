package com.santint.mypermission;

/**
 * author： cyw
 */
public interface IPermissionListener {
    void ganted(); // 已经授权

    void cancel(); // 取消权限

    void denied(); // 拒绝权限
}
