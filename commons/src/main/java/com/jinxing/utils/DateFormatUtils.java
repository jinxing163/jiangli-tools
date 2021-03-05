package com.jinxing.utils;


import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期转换工具类
 *
 * @author JinXing
 * @date 2018/7/18 14:53
 */
public class DateFormatUtils {



    /**
     * 日期格式定义
     */
    public enum FormatEnum {

        //枚举定义
        YYYY("yyyy", "年"),
        YYYY_MM_DD("yyyy-MM-dd", "年月日"),
        YYYY_MM_DD_00_00_00("yyyy-MM-dd 00:00:00", "年月日0时0分0秒"),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", "年月日时分"),
        YYYYMMDDHHMMSS("MMddHHmmssSSS", "年月日时分秒毫秒"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", "年月日时分秒"),
        Y_M_D("yyyy.MM.dd", "年月日"),
        M_D("MM月dd日", "月日"),

        ;
        private final String code;
        private final String declare;

        public String getCode() {
            return code;
        }

        public String getDeclare() {
            return declare;
        }

        FormatEnum(String code, String declare) {
            this.code = code;
            this.declare = declare;
        }
    }


    /**
     * 时间单位定义：年月日时分秒
     */
    public enum UnitEnum {

        //枚举定义
        SECOND,
        MINUTE,
        HOURS,
        DAY,
        MONTH,
        YEAR,
    }


    ///////////////////////////// 日期转换方法 /////////////////////////////

    public static void main(String[] args) throws ParseException {
//        Date parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-07-15 00:00:00");
//        Date parse2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2020-07-14 00:00:00");
//
//        int validityDay = 30;
//        int daysBetween = daysBetween(parse2.getTime(), parse1.getTime(), false);
//        if (daysBetween <= 0) {
//            System.out.println(3);
//        } else if (daysBetween <= validityDay) {
//            System.out.println(2);
//        } else {
//            System.out.println(1);
//        }
        System.out.println("日期=" + getWeekDate());
    }

    /**
     * Date转字符串
     */
    public static String dateToString(Date parse, FormatEnum format) {
        String toStr = null;
        try {
            SimpleDateFormat sdf2 = new SimpleDateFormat(format.getCode());
            toStr = sdf2.format(parse);
        } catch (Exception e) {
            FormatEnum[] values = FormatEnum.values();
            for (FormatEnum formatEnum : values) {
                try {
                    SimpleDateFormat sdf2 = new SimpleDateFormat(formatEnum.getCode());
                    toStr = sdf2.format(parse);
                    return toStr;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }

        return toStr;
    }

    /**
     * 字符串转Date
     */
    public static Date stringToDate(String strTime, FormatEnum format) {

        Date toDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format.getCode());
            toDate = formatter.parse(strTime);
        } catch (Exception e) {
            FormatEnum[] values = FormatEnum.values();
            for (FormatEnum formatEnum : values) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(formatEnum.getCode());
                    toDate = formatter.parse(strTime);
                    if (toDate != null) {
                        return toDate;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return toDate;
    }

    /**
     * 字符串转Long
     */
    public static Long stringToLong(String strTime, FormatEnum format) throws ParseException {
        Date date = stringToDate(strTime, format);
        return date != null ? date.getTime() : null;
    }

    /**
     * Long转date
     */
    public static Date longToDate(long timeInMillis) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(timeInMillis);
        return now.getTime();
    }

    /**
     * Long转String
     */
    public static String longToString(long timeInMillis, FormatEnum format) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(timeInMillis);
        return dateToString(now.getTime(), format);
    }


    /**
     * 未来时间返回
     *
     * @param strTime  字符串日期时间
     * @param format   字符串日期格式
     * @param unitEnum 增加的时间单位
     * @param addTime  增加的时间个数  比如：unitEnum=SECOND,addTime=1 表示查询一秒之后的时间
     */
    public static String afterTimeAndReturn(String strTime, FormatEnum format, UnitEnum unitEnum, int addTime) throws ParseException {

        Date date = stringToDate(strTime, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        checkUnitEnum(unitEnum, addTime, calendar);

        return dateToString(calendar.getTime(), format);
    }


    /**未来时间返回*/
    public static Date afterDataAndReturn(String strTime, FormatEnum format, UnitEnum unitEnum, int addTime) throws ParseException {

        Date date = stringToDate(strTime, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        checkUnitEnum(unitEnum, addTime, calendar);

        return calendar.getTime();
    }

    private static void checkUnitEnum(UnitEnum unitEnum, int addTime, Calendar calendar) {
        if (unitEnum == UnitEnum.SECOND) {
            calendar.add(Calendar.SECOND, addTime);
        } else if (unitEnum == UnitEnum.MINUTE) {
            calendar.add(Calendar.MINUTE, addTime);
        } else if (unitEnum == UnitEnum.HOURS) {
            calendar.add(Calendar.HOUR, addTime);
        } else if (unitEnum == UnitEnum.DAY) {
            calendar.add(Calendar.DAY_OF_YEAR, addTime);
        } else if (unitEnum == UnitEnum.MONTH) {
            calendar.add(Calendar.MONTH, addTime);
        } else if (unitEnum == UnitEnum.YEAR) {
            calendar.add(Calendar.YEAR, addTime);
        }
    }


    /**
     * 将当前时间转为 当前时间的 00:00:00
     */
    public static String currentTimeForStart(long currentTimeMillis, FormatEnum formatEnum) throws ParseException {

        long millis = currentTimeMillisForStart(currentTimeMillis);

        return longToString(millis, formatEnum);
    }

    /**
     * 将当前时间转为 当前时间的 00:00:00
     */
    public static long currentTimeMillisForStart(long currentTimeMillis) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().getTime();
    }

    /**
     * 将当前时间转为 当前时间的 23:59:59
     */
    public static String currentTimeForEnd(long currentTimeMillis, FormatEnum formatEnum) throws ParseException {

        long millis = currentTimeMillisForEnd(currentTimeMillis);

        return longToString(millis, formatEnum);
    }

    /**
     * 将当前时间转为 当前时间的 23:59:59
     */
    public static long currentTimeMillisForEnd(long currentTimeMillis) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        // 分
        calendar.set(Calendar.MINUTE, 59);
        // 秒
        calendar.set(Calendar.SECOND, 59);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().getTime();
    }


    /**
     * 求两个时间的时间差
     */
    public static int daysBetween(long time1, long time2) {
        return daysBetween(time1, time2, true);
    }

    public static int daysBetween(long time1, long time2, boolean abs) {
        long time = time1 - time2;
        if (abs) {
            time = Math.abs(time1 - time2);
        }
        return (int) (time / (24 * 60 * 60 * 1000));
    }




    /**
     * 当前时间所在一周的周一和周日时间
     *
     * @return
     */
    public static Map<String, Date> getWeekDate() {
        Map<String, Date> map = new HashMap(20);
        Calendar cal = Calendar.getInstance();
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
        Date sundayDate = cal.getTime();
        map.put("mondayDate", dateBeginOrEnd(mondayDate, true));
        map.put("sundayDate", dateBeginOrEnd(sundayDate, false));
        return map;
    }

    /**
     * 获取当前月第一天
     *
     * @return String
     */
    public static String getFirstDayOfMonth(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int month = calendar.get(Calendar.MONTH) + 1;

        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最小天数
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最小天数
        calendar.set(Calendar.DAY_OF_MONTH, firstDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 00:00:00";
    }


    /**
     * 获取当前月最后一天
     *
     * @return String
     */
    public static String getLastDayOfMonth(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);

        int month = calendar.get(Calendar.MONTH) + 1;

        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = 0;
        //2月的平年瑞年天数
        if (month == 2) {
            lastDay = calendar.getLeastMaximum(Calendar.DAY_OF_MONTH);
        } else {
            lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        // 设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime()) + " 23:59:59";
    }


    private static void aaa() {
        //在X月X日（星期几）晚xx：xx时

        Date date = new Date();

        String[] weeks = getWeeks();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }

        //获取月份，0表示1月份
        int month = cal.get(Calendar.MONTH) + 1;
        //获取当前天数
        int day = cal.get(Calendar.DAY_OF_MONTH);
        //获取当前小时
        int time = cal.get(Calendar.HOUR_OF_DAY);
        //获取当前分钟
        int min = cal.get(Calendar.MINUTE);


        String str = "上午";
        if (time > 12 && time <= 17) {
            str = "下午";
        } else if (time > 17) {
            str = "晚上";
        }
        System.out.println(month + "月" + day + "日(" + weeks[week_index] + ")");
        System.out.println(str + time + ":" + min);
    }

    private static String[] getWeeks() {
        return new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    }

    public static Date dateBeginOrEnd(Date date, Boolean flag) {
        Calendar calendar1 = Calendar.getInstance();
        //获取某一天的0点0分0秒 或者 23点59分59秒
        if (flag == true) {
            calendar1.setTime(date);
            calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                    0, 0, 0);
        } else {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date);
            calendar1.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
        }
        return calendar1.getTime();
    }

    public static String getWeek(Date date) {
        String[] weeks = getWeeks();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }
}
