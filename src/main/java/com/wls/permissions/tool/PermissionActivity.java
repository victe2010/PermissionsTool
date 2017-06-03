package com.wls.permissions.tool;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * Created by 13526 on 2017/6/3.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public final class PermissionActivity extends Activity {
    public static final String permissions_key = "pms_key";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = getIntent().getStringArrayExtra(permissions_key);
        if (permissions != null && mlistener != null){
              Log.e("TAG",permissions[0]);
              requestPermissions(permissions,1);
        }
        else finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mlistener != null)
             mlistener.onRequestPermissionResult(permissions, grantResults);
        mlistener = null;
        finish();

    }

    private static PermissionListener mlistener;
    public static void setPermissionListeren(PermissionListener listener){
        mlistener = listener;
    }

    public interface PermissionListener{
        void onRequestPermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults);
    }
}
