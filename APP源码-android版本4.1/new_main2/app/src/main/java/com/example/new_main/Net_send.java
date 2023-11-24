package com.example.new_main;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.URL;

public class Net_send {
    private Context context;
    private WifiManager wifiManager;
    private Socket socket;
    private OutputStream outputStream;


    public void  MyWiFiService(Context context) {

        this.context = context;

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

    }



    public void connectToEsp32acm(String IP,int port) {

        // 连接到ESP32-ACM的IP地址和端口号

        String ipAddress = IP; // ESP32-ACM的IP地址
        int portNumber = port; // ESP32-ACM的端口号
        socket = new Socket();
        try {
            socket.connect((new InetSocketAddress(ipAddress, portNumber)), 3000);

        } catch (IOException e) {

            e.printStackTrace();

        }

        try {

            outputStream = socket.getOutputStream();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }



    public void sendDataToEsp32acm(String data) {

        // 将数据发送到ESP32-ACM

        try {

            outputStream.write(data.getBytes());

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}