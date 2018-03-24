package mao.com.maocriminalintent.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maoqi on 2018/2/25 0025.
 * 工具类
 */

public class MyUtils {

    /**
     * 格式化日期格式
     * @param date
     * @return yyyy年MM月dd日 HH时mm分ss秒
     */
    public static String getFormatDate(Date date){
        Locale locale = new Locale("zh", "CN");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE-yyyy年MM月dd日 HH时mm分ss秒",locale);
        return formatter.format(date);
    }


    /**
     * 手动缩放位图照片
     * @param path
     * @param destWidth
     * @param destHeight
     * @return
     */
    public static Bitmap  getScaledBitmap(String path, int destWidth, int destHeight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize=1;
        if(srcHeight>destHeight || srcWidth>destWidth){
           float heightScale=srcHeight/destHeight;
            float widthScale=srcWidth/destWidth;
            inSampleSize=Math.round(heightScale>widthScale? heightScale:widthScale);
        }
        options=new BitmapFactory.Options();
        options.inSampleSize=inSampleSize;
        return  BitmapFactory.decodeFile(path,options);
    }

    /**
     * 保守预估值缩放(该方法先确认屏幕的尺寸，然后按此缩放图像。这样，就能保证载入的ImageView永远不会 过大。)
     * @param path
     * @param activity
     * @return
     */
    public static Bitmap  getScaledBitmap(String path, Activity activity){
        Point point=new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return getScaledBitmap(path,point.x,point.y);
    }
}
