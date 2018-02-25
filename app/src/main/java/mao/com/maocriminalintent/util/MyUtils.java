package mao.com.maocriminalintent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return formatter.format(date);
    }
}
