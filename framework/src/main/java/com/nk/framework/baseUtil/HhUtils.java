package com.nk.framework.baseUtil;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dax on 2016/7/11.
 */
public class HhUtils {
    static DecimalFormat dFormat = new DecimalFormat("0.00");

    public static String formatNumber(double number) {
        try {
            return dFormat.format(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(number);

    }


    public static String formatNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return "0.00";
        }
        try {
            return dFormat.format(new BigDecimal(number));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return number;
    }



    /**
     * 格式化百分比
     * @return
     */
    public static String formatPercent(double d) {
        return new DecimalFormat("0%").format(d);
    }


    /**
     * 解析时间 "yyyy-MM-dd HH:mm:ss" 格式
     * @param time
     * @return
     */

    public static long parseTimeForyyyyMMddHHmmss(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 将map 转为 string
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>(map.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = map.get(key).toString();
            sb.append(key + "=" + value);
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.lastIndexOf("&"));
        }
        return s;
    }

}

