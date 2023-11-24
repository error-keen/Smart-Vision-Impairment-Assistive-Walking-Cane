package com.example.new_main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class permisson {
    public final  static  int COOE_RECERD_AUDIO=0;
    public final  static  int COOE_GET_ACC=1;
    public final  static  int COOE_READ_PHONE_STATE=2;
    public final  static  int COOE_CALL_PHONE=3;
    public final  static  int COOE_CAMAR=4;
    public final  static  int COOE_ACCESS_FINE_LOCATTON=5;
    public final  static  int COOE_ACCESS_COARSE_LOCATION=6;
    public final  static  int COOE_READ_EXTERNAL_STORAGE=7;
    public final  static  int COOE_WRITE_EXTERNAL_STORAGE=8;

    public  final  static  String PEMISSION_RECERD_AUDIO= Manifest.permission.RECORD_AUDIO;
    public  final  static  String PEMISSION_GET_ACC= Manifest.permission.GET_ACCOUNTS;
    public  final  static  String PEMISSION_READ_PHONE_STATE= Manifest.permission.READ_PHONE_STATE;
    public  final  static  String PEMISSION_CALL_PHONE= Manifest.permission.CALL_PHONE;
    public  final  static  String PEMISSION_CAMEAR= Manifest.permission.CAMERA;
    public  final  static  String PEMISSION_ACCESS_FINE_LOCATTON= Manifest.permission.ACCESS_FINE_LOCATION;
    public  final  static  String PEMISSION_ACCESS_COARSE_LOCATION= Manifest.permission.ACCESS_COARSE_LOCATION;
    public  final  static  String PEMISSION_READ_EXTERNAL_STORAGE= Manifest.permission.READ_EXTERNAL_STORAGE;
    public  final  static  String PEMISSION_WRITE_EXTERNAL_STORAGE= Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private  static  final  String[] requestPermissions={PEMISSION_RECERD_AUDIO,PEMISSION_GET_ACC,PEMISSION_READ_PHONE_STATE,
            PEMISSION_CALL_PHONE,PEMISSION_CAMEAR,PEMISSION_ACCESS_FINE_LOCATTON,PEMISSION_ACCESS_COARSE_LOCATION,
            PEMISSION_READ_EXTERNAL_STORAGE,PEMISSION_WRITE_EXTERNAL_STORAGE

    };

    public interface  PermissionGrant{
        void onPermissionGranted (int requestCode);

    }
    public static void requestPermission(Activity activity, int requestCode, PermissionGrant permissionGrant){
        if(activity==null){

            return;
        }
        if(requestCode<0||requestCode>=requestPermissions.length){

            return;
        }
        String requestPermission = requestPermissions[requestCode];
        if(Build.VERSION.SDK_INT>=23){
            return;

        }
        int checkSelfPermission;
        try{
            checkSelfPermission= ActivityCompat.checkSelfPermission(activity,requestPermission);

        }catch (Exception e){
            Toast.makeText(activity,"请打开这个权限"+requestPermission,Toast.LENGTH_SHORT).show();
            return;
        }

        if(checkSelfPermission!= PackageManager.PERMISSION_DENIED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,requestPermission))
            {
                shouldShowRationle(activity,requestCode,requestPermission);
            }
            else
            {
                ActivityCompat.requestPermissions(activity,new String[]{requestPermission},requestCode);
            }
        }
        else {
            Toast.makeText(activity,"opened"+requestPermission,Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(requestCode);
        }

    }



    public static void RequestPermissionsResult(Activity activity, int requestCode,  @NonNull String[] permissions,
                                                @NonNull int[] grantResults,PermissionGrant permissionGrant)
    {
        if(activity==null)
        {
            return;
        }
        if (requestCode<0||requestCode>=requestPermissions.length)
        {
            Toast.makeText(activity,"illegal requestCode:"+requestCode,Toast.LENGTH_SHORT).show();
            return;
        }
        if (grantResults.length==1&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            permissionGrant.onPermissionGranted(requestCode);
        }
        else {
            String permissionError=permissions[requestCode];
            openSettingActivity(activity,"Result"+permissionError);
        }
    }

    private static void openSettingActivity(Activity activity, String msg) {
        showMessageOKCancel(activity, msg, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                Uri uri=Uri.fromParts("package",activity.getPackageName(),null);
                intent.setAction(String.valueOf(uri));
                activity.startActivity(intent);
            }
        });
    }
    private  static  void  showMessageOKCancel(Activity activity, String msg, DialogInterface.OnCancelListener oklistener)
    {
        new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) oklistener)
                .setNegativeButton("Cancel",null)
                .create().show();
    }
    private static void shouldShowRationle(Activity activity, int requestCode, String requestPermission) {
        showMessageOKCancel(activity, "Ration:" + requestPermission, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ActivityCompat.requestPermissions(activity,new String[]{requestPermission},requestCode);
            }
        });
    }
}
