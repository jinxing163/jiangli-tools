package com.jinxing.helper;

import com.alibaba.druid.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日志转换工具类
 *
 * @author JinXing
 * @date 2018/7/18 14:53
 */
public class DateFormatHelper {



    public enum Format {

        /** 日期格式 */
        YEAR_MM_DD("yyyy-MM-dd", "年月日"),
        YEAR_MM_DD_HH_MM("yyyy-MM-dd HH:mm", "年月日时分"),
        YEAR_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", "年月日时分秒"),;
        private String code;
        private String declare;

        public String getCode() {
            return code;
        }

        public String getDeclare() {
            return declare;
        }

        Format(String code, String declare) {
            this.code = code;
            this.declare = declare;
        }
    }


    public enum Unit {

        /** 时间单位类型 */
        UNIT_SECONDS("seconds", "秒"),
        UNIT_MINUTES("minutes", "分钟"),
        UNIT_HOURS("hours", "小时"),
        UNIT_DAYS("days", "天"),
        UNIT_MONTH("days", "天"),;

        /** 单位名称 */
        private String code;
        /** 单位说明 */
        private String message;

        Unit(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }


    /** Date转字符串 */
    public static String DateToString(Date parse, Format format) throws ParseException {

        format = defaultFormat(null, format);

        SimpleDateFormat sdf2 = new SimpleDateFormat(format.code);
        return sdf2.format(parse);
    }

    public static Date stringToDate(String strTime, Format format)
            throws ParseException {

        format = defaultFormat(strTime, format);

        SimpleDateFormat formatter = new SimpleDateFormat(format.code);
        return formatter.parse(strTime);
    }

    /** 字符串转Long */
    public static Long stringToLong(String strTime, Format format) throws ParseException {
        if (StringUtils.isEmpty(strTime)) {
            return 0L;
        }

        format = defaultFormat(strTime, format);

        Date date = stringToDate(strTime, format);
        if (null == date) {
            return null;
        }
        return date.getTime();
    }

    /** 默认时间格式 */
    private static Format defaultFormat(String value, Format format) {

        if (format != null) {
            return format;
        } else if (StringUtils.isEmpty(value)) {
            return Format.YEAR_MM_DD_HH_MM_SS;
        }

        int len1 = 10, len2 = 16, len3 = 19;
        String trimValue = value.trim();

        if (trimValue.length() >= len1 && trimValue.length() < len2) {
            //格式：年月日
            return Format.YEAR_MM_DD;
        } else if (trimValue.length() >= len2 && trimValue.length() < len3) {
            //格式：年月日
            return Format.YEAR_MM_DD_HH_MM;
        } else if (trimValue.length() >= len3) {
            return Format.YEAR_MM_DD_HH_MM_SS;
        }

        return Format.YEAR_MM_DD_HH_MM_SS;
    }

    public static Date calculateAfterTime(Unit unit, Long timeInMillis, Integer number) {
        Calendar now = Calendar.getInstance();
        if (timeInMillis != null) {
            //指定时间戳
            now.setTimeInMillis(timeInMillis);
        }
        if (Unit.UNIT_SECONDS == unit) {
            // second
            now.add(Calendar.SECOND, number);
        } else if (Unit.UNIT_MINUTES == unit) {
            // minute
            now.add(Calendar.MINUTE, number);
        } else if (Unit.UNIT_HOURS == unit) {
            // hour
            now.add(Calendar.HOUR, number);
        } else if (Unit.UNIT_DAYS == unit) {
            // day
            now.add(Calendar.DAY_OF_YEAR, number);
        } else {
            return null;
        }
        return now.getTime();
    }


    /**
     * 获取当天开始时间
     *
     * @return
     *
     * @author zhangys
     * @date 2017年9月2日 上午11:11:23
     */
    public static String getTodayStartTime(Date date,Format format) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date ==null ? new Date():date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return DateToString(calendar.getTime(), format);
    }

    /**
     * 获取当天结束时间
     *
     * @return
     *
     * @author zhangys
     * @date 2017年9月2日 上午11:11:50
     */
    public static String getTodayEndTime(Date date,Format format) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date ==null ? new Date():date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return DateToString(calendar.getTime(),format);
    }


    public static void main(String[] args) throws ParseException {
        Date date = calculateAfterTime(Unit.UNIT_SECONDS, null, 3);
        System.out.println("now:" + DateToString(new Date(), Format.YEAR_MM_DD_HH_MM_SS));
        System.out.println("after:" + DateToString(date, Format.YEAR_MM_DD_HH_MM_SS));
    }


}
