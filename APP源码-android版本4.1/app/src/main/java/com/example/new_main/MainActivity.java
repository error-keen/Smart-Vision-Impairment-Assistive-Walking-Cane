package com.example.new_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.new_main.Net_send;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import static android.os.Build.VERSION_CODES.M;
import static androidx.core.content.ContextCompat.startActivity;

public class MainActivity extends AppCompatActivity {
    private static Bitmap selubitmap,src_bmp,dest_bmp;
    private static boolean tone = true,LED=false;
    private Button but_phone,but_seek,but_note,but_recognize,but_LED;
    private static TextView tv1,tv2;
    private static ImageView iv1,Image_test;
    private static Mat test;
    private static String location;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniLoadOpenCV();
        init();
        init_location();
        but_recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent((MainActivity.this),Activity_recognition.class);
                startActivity(intent);
                start2();

            }
        });
        but_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            call("19838272959");
            }
        });
        but_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note();

            }
        });
        but_seek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Net_Utils.get_buzzer();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                thread.start();
                if(!tone) {
                    but_seek.setText("寻找导盲杖");
                    tone=!tone;
                }
                else {
                    but_seek.setText("关闭寻找");
                    tone=!tone;
                }
            }
        });
        but_LED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(LED) {
                                    Net_Utils.get_close_LED();
                                    LED = !LED;
                                }
                                else
                                {
                                    Net_Utils.get_open_LED();
                                    LED=!LED;
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    thread.start();
                if(LED) {
                    but_LED.setText("夜间模式");
                }
                else {
                    but_LED.setText("日间模式");

                }
            }

        });

    }
    private void init()
    {
        but_note=findViewById(R.id.note);
        but_phone=findViewById(R.id.phone);
        but_seek=findViewById(R.id.seek);
        but_recognize=findViewById(R.id.recognize);
        but_LED=findViewById(R.id.LED);
/*      iv1=findViewById(R.id.imageview);
        tv1=findViewById(R.id.textView);*/
        tv2=findViewById(R.id.textView2);
        //Image_test=findViewById(R.id.imageview);
        nightsuny();

    }
    /*public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.recognize:
                src_bmp=((BitmapDrawable)((ImageView)iv1).getDrawable()).getBitmap();
                test = opencv_process.perset(src_bmp);
                Bitmap dest_bmp=Bitmap.createBitmap(test.width(),test.height(),Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(test,dest_bmp);
                iv1.setImageBitmap(dest_bmp);
                break;

        }
    }*/
    private void back() {
        Intent intent=new Intent(this, MainActivity.class );
        startActivity(intent);
    }











    private void start() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1;
                try {
                    bitmap1 = Net_Utils.doGet_image();
                    Message message = new Message();
                    message.what = 0;
                    message.obj = bitmap1;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    private static Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Bitmap bitmap1 = (Bitmap) msg.obj;
                test=opencv_process.perset(bitmap1);
                Bitmap dest_bmp=Bitmap.createBitmap(test.width(),test.height(),Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(test,dest_bmp);
            }
        }
    };




    private void iniLoadOpenCV()
    {
        OpenCVLoader.initDebug();
    }



    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码
    public static final int PERMISSIONS_REQUEST_SEND_SMS = 10112;

    /**
     * 判断是否有某项权限
     * @param string_permission 权限
     * @param request_code 请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    /**
     * 检查权限后的回调
     * @param requestCode 请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(this,"请允许拨号权限后再试",Toast.LENGTH_SHORT).show();
                } else {//成功
                    call("tel:"+"19838272959");
                }
                break;
            case PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, send the SMS
                    sendSms();
                } else {
                    // Permission was denied, handle the case where it was not granted
                    Toast.makeText(getApplicationContext(), "请允许短信权限后再试", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    /**
     * 拨打电话（直接拨打）
     * @param telPhone 电话
     */
    public void call(String telPhone){
        if(checkReadPermission(Manifest.permission.CALL_PHONE,REQUEST_CALL_PERMISSION)){
            Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(telPhone));
            Uri uri = Uri.parse("tel:19838272959");
            intent.setData(uri);
            startActivity(intent);
        }

    }
    public void note() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_SEND_SMS);
        } else {

            // Permission is already granted, send the SMS
            init_location();
            sendSms();
        }

    }



    private static Handler mHandler2 = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
    private void sendSms() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:19838272959"));
        intent.putExtra("sms_body",location+"——————我需要帮助");
        startActivity(intent);

    }

    private void init_location() {
        //初始化定位
        try {
            AMapLocationClient.updatePrivacyShow(this, true, true);
            AMapLocationClient.updatePrivacyAgree(this, true);
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }
    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Log.d("位置：", aMapLocation.getAddress());
                    tv2.setText(aMapLocation.getAddress());
                    location=aMapLocation.getAddress();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.d("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    }
    private void nightsuny()
    {
        if(time_tool.checkDayOrNight())
        {
            Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Net_Utils.get_close_LED();
                    LED=false;
                    but_LED.setText("日间模式");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
            thread.start();
        }
        else  if(time_tool.checkDayOrNight())
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Net_Utils.get_open_LED();
                        LED=true;
                        but_LED.setText("夜间模式");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }
    }
    private void start2() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1;
                try {
                    bitmap1 = Net_Utils.doGet_image();
                    Message message = new Message();
                    message.what = 0;
                    message.obj = bitmap1;
                    mHandler3.sendMessage(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }
    private static Handler mHandler3 = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Bitmap bitmap1 = (Bitmap) msg.obj;
                Activity_recognition.IMG.setImageBitmap(bitmap1);
            }
        }
    };
}


