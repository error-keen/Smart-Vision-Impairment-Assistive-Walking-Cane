package com.example.new_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class Activity_recognition extends AppCompatActivity {
    public static   TextView test;
    private static   Button button;
    public static ImageView IMG;
    private static Mat test_bit;
    private static Bitmap selubitmap,src_bmp,dest_bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        test=findViewById(R.id.Test_ruslt);
        button =findViewById(R.id.Button_re);
        iniLoadOpenCV();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start();
            }
        });
        IMG=findViewById(R.id.imageView_1);
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
                test_bit=opencv_process.perset(bitmap1);
                Bitmap dest_bmp=Bitmap.createBitmap(test_bit.width(),test_bit.height(),Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(test_bit,dest_bmp);
                IMG.setImageBitmap(dest_bmp);
            }
        }
    };

    private void iniLoadOpenCV()
    {
        OpenCVLoader.initDebug();
    }





}