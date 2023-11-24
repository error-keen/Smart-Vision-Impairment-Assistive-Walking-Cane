package com.example.new_main;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.media.Image;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;


public class opencv_process extends Activity_recognition {
    private static List<MatOfPoint> contours1 =new ArrayList<>();
    private static List<MatOfPoint> contours2 =new ArrayList<>();

    //图像缩放
    public  static Mat perset(Bitmap src){
        Mat dest,re_dest;
        dest=new Mat(src.getWidth(),src.getHeight(), CvType.CV_8UC3);
        Utils.bitmapToMat(src,dest);
        Imgproc.resize(dest,dest,new Size(1600,1200),Imgproc.INTER_NEAREST);
        Rect rect=new Rect(13,11,1580,700);//新建矩形框对象
        re_dest = new Mat(dest,rect); //切割出矩形框的内容
        Imgproc.GaussianBlur(re_dest,re_dest,new Size(7,7),0);
        Imgproc.blur(re_dest,re_dest,new Size(4,4));
        re_dest.convertTo(re_dest,-1,1.3,1.1);
        //做开闭运算
        Mat kernel=Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3,3));
        Mat hsv1=new Mat();
        Mat hsv2=new Mat();
        Mat hsv3=new Mat();
        Imgproc.cvtColor(re_dest,hsv1,Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(re_dest,hsv3,Imgproc.COLOR_RGB2HSV);
        Imgproc.cvtColor(re_dest,hsv2,Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsv1, new Scalar(108,40,40),new Scalar(130,255,255),hsv1);//将hsv图红色范围转为二值图
        Core.inRange(hsv2, new Scalar(40,40,40),new Scalar(60,240,255),hsv2);//将绿色图像转为二值图
        //对自身开运算
        //对自身闭运算
        Imgproc.morphologyEx(hsv1,hsv1,Imgproc.MORPH_CLOSE,kernel);
        Imgproc.morphologyEx(hsv1,hsv1,Imgproc.MORPH_OPEN,kernel);
        //对自身开运算
        //对自身闭运算
        Imgproc.morphologyEx(hsv2,hsv2,Imgproc.MORPH_CLOSE,kernel);
        Imgproc.morphologyEx(hsv2,hsv2,Imgproc.MORPH_OPEN,kernel);
        int count1 = Core.countNonZero(hsv1);
        int count2 = Core.countNonZero(hsv2);
        System.out.println(count1);
        System.out.println(count2);
        Mat outmat=new Mat();
        /*Imgproc.findContours(hsv1,contours1,outmat,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.findContours(hsv2,contours2,outmat,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);*/
        if(count1 > count2 )
        {
            test.setText("未检测到");//红
        }
        else
            test.setText("未检测到");
        return dest;
    }
}
