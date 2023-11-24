package com.example.new_main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Net_Utils {

    public static Bitmap doGet_image() {
        String url1 = "http://192.168.43.222/cam-lo.jpg";
        String url2 = "http://192.168.43.82/cam-lo.jpg";
        String url3 = "http://192.168.43.175/cam-lo.jpg";
        Bitmap bitmap;
        try {
            HttpURLConnection httpURLConnection = null;
            URL requestUrl = new URL(url1);
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }
    public static void get_buzzer()
    {
        String url1 = "http://192.168.43.222/cam_on_or_off";
        String url2 = "http://192.168.43.82/cam_on_or_off";
        String url3 = "http://192.168.43.175/cam_on_or_off";
        try {
            HttpURLConnection httpURLConnection = null;
            URL requestUrl = new URL(url1);
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.connect();
            //InputStream inputStream = httpURLConnection.getInputStream();
            // 获取服务器响应的状态码，200表示成功
            httpURLConnection.getResponseCode();
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
            // 获取输入流对象
//                InputStream inputStream = httpURLConnection.getInputStream();
//                // 使用InputStreamReader和BufferedReader读取数据
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                String line;
//                StringBuilder response = new StringBuilder();
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//                // 打印响应数据
//                Log.d("", "Response: " + response.toString());
//                // 关闭流和连接
//                reader.close();
//                inputStream.close();
//                httpURLConnection.disconnect();
//            } else {
//                Log.d("", "Request failed with response code: " + responseCode);
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void get_close_LED()
    {

        String url1 = "http://192.168.43.222/close_LED";
        String url2 = "http://192.168.43.82/close_LED";
        String url3 = "http://192.168.43.175/close_LED";
        try {
            HttpURLConnection httpURLConnection = null;
            URL requestUrl = new URL(url1);
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.connect();
            httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void get_open_LED()
    {
        String url1 = "http://192.168.43.222/open_LED";
        String url2 = "http://192.168.43.82/open_LED";
        String url3 = "http://192.168.43.175/open_LED";

        try {
            HttpURLConnection httpURLConnection = null;
            URL requestUrl = new URL(url1);
            httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.connect();
            httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
