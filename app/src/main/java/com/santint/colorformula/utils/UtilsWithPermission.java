package com.santint.colorformula.utils;

import android.Manifest;
import android.app.Activity;

import com.cyw.mylibrary.util.CheckPermissionsAdapter;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.santint.colorformula.BlueToothActivity;


/**
 * @author cd5160866
 */
public class UtilsWithPermission {
    private static boolean hasReadWrite = false;

    /**
     * 拨打指定电话
     */
//    public static void makeCall(final Context context, final String phoneNumber) {
//        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CALL_PHONE,
//                new CheckPermissionWithRationaleAdapter("如果你拒绝了权限，你将无法拨打电话，请点击授予权限",
//                        new Runnable() {
//                            @Override
//                            public void run() {
//                                //retry
//                                makeCall(context, phoneNumber);
//                            }
//                        }) {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onPermissionOk(Permission permission) {
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        Uri data = Uri.parse("tel:" + phoneNumber);
//                        intent.setData(data);
//                        if (!(context instanceof Activity)) {
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        }
//                        context.startActivity(intent);
//                    }
//                });
//    }


    /**
     * 读权限
     */
    public static boolean readWriteStorage(final Activity activity, final int requestCode) {

        String[] ps = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        com.qw.soul.permission.bean.Permissions permissions = com.qw.soul.permission.bean.Permissions.build(ps);
        SoulPermission.getInstance().checkAndRequestPermissions(permissions, new CheckPermissionsAdapter() {
            @Override
            public void onAllPermissionOk(Permission[] allPermissions) {
                hasReadWrite = true;
//                CannedResultService.getInstance().initDao(activity);
//                ResultDataService.getInstance().initDao(activity);
//                ResultBreakService.getInstance().initDao(activity);
//                FormulaDataService.getInstance().initDao(activity);
//                ColorBeanService.getInstance().initDao(activity);
//                PumpConfigBeanService.getInstance().initDao(activity);
//                FMServiceNew.getInstance().initDao(activity);
//                DDService.getInstance().initDao(activity);
                ((BlueToothActivity) activity).permissionBack();

            }
        });
        return hasReadWrite;
    }


}
