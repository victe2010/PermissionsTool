package com.wls.permissions.tool;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.wls.permissions.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by wls on 2017/3/29 18:02.
 */

public class PermissionTool {
    private  Activity activity;
    private String[] permissions;//请求的权限
    private CallBack callBack;//回掉接口
    private int requestCode;

    private static PermissionTool permissionTool;
    public static PermissionTool getInstance(){
        if (permissionTool == null) permissionTool = new PermissionTool();
        return permissionTool;
    }


    //当前activity对象
    public PermissionTool with(Activity activity){
        this.activity = activity;
        return this;
    }
    //设置请求的权限
    public PermissionTool requestpermissions(String... permissions){
        this.permissions = permissions;
        return this;
    }
    //回调
    public PermissionTool callBack(CallBack callBack){
        this.callBack = callBack;
        return this;
    }

    //设置请求码
    public PermissionTool requestCode(int requestCode){
        this.requestCode = requestCode;
        return this;
    }

    //开始请求
    public void start(){
        if (permissions == null) return;
        if (activity == null) return;
        if (callBack == null) return;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            callBack.onSuccess(requestCode, Arrays.asList(permissions));
        }
        else
        {
            //检测权限-获取没有授权的权限
            String[] deniedPermissions = checkPermission(activity,permissions);
            if (deniedPermissions == null){
                //全部已授权
                callBack.onSuccess(requestCode,Arrays.asList(permissions));
            }
            else{
                PermissionActivity.setPermissionListeren(listener);
                //含有未授权的权限
                Intent intent = new Intent(activity,PermissionActivity.class);
                intent.putExtra(PermissionActivity.permissions_key,deniedPermissions);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        }
    }
    //申请权限后的回调
   private PermissionActivity.PermissionListener listener = new PermissionActivity.PermissionListener() {
       @Override
       public void onRequestPermissionResult(@NonNull String[] permissions_listeren, @NonNull int[] grantResults) {
            List<String> denies = new ArrayList<>();//拒绝的权限
           for (int i = 0; i < grantResults.length; i++) {
               if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                   //没有授权
                   denies.add(permissions_listeren[i]);
               }
           }
           if (denies.isEmpty()) callBack.onSuccess(requestCode,Arrays.asList(permissions));
           else {
             callBack.onFail(requestCode,denies);
           }
       }
   };

    /***
     *  检测是否具有permission权限
     * @param context
     * @param permissions
     * @return 返回没有授权的权限数组
     */
    private  String[] checkPermission(Context context,String... permissions){
        ArrayList<String> list = new ArrayList<>();
        for (String permission:permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission
                    (context, permission);
            if(PackageManager.PERMISSION_GRANTED != permissionCheck){
                //没有授权的permission
                list.add(permission);
            }
        }
        if (list.isEmpty()) return null;
        //返回没有授权的权限
        else return list.toArray(new String[list.size()]);

    }

    /**
     * 打开用户权限设置
     * @param context
     */
    private static void openPermissionDenity(Context context){
        context.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
    }

    /**
     * 显示是否前往权限管理的对话框
     * context 上下文对象
     * message 提示消息
     * @param context message
     */
    public void showDialog(final Context context,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示信息");
        builder.setMessage(message);
        builder.setPositiveButton("前往", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openPermissionDenity(context);
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

    private PermissionTool(){}

    /**
     * 权限回掉
     */
    public interface CallBack{
        void onSuccess(int requestCode, List<String> grantPermissions);
        void onFail(int requestCode, List<String> deniedPermissions);
    }

}
