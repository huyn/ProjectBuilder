package com.huyn.projectbuilder.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author huyn
 * @version 2013-12-9 下午4:43:26
 */

public class DateUtil {
    public static final long m_second = 1000;
    public static final long m_minute = m_second * 60;
    public static final long m_hour = m_minute * 60;
    public static final long m_day = m_hour * 24;

    public static long GEWARA_TIME = System.currentTimeMillis();

    public static long LOCAL_TIME = System.currentTimeMillis();

    /**
     * <p>DateUtil instances should NOT be constructed in standard programming.</p>
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     */
    public DateUtil() {
    }

    public static final long timeMillis() {
        return GEWARA_TIME;
    }

    public static long setGewaraTime(long time) {
        GEWARA_TIME = time;
        return GEWARA_TIME;
    }

    public static void setLocalTime(long local) {
        LOCAL_TIME = local;
    }


    public static final Date currentTime() {
        return new Date(GEWARA_TIME);
    }

    public static final Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static final int nextMonth() {
        String next = format(new Date(DateUtil.GEWARA_TIME), "M");
        int nextMonth = Integer.parseInt(next) + 1;
        if (nextMonth == 13) return 1;
        return nextMonth;
    }

    /**
     * parse date using default pattern yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static final Date parseDate(String strDate) {
        Date date = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.parse(strDate);
            return date;
        } catch (Exception pe) {
            return null;
        }
    }

    public static final Timestamp parseTimestamp(String strDate) {
        try {
            // Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]
            Timestamp result = Timestamp.valueOf(strDate);
            return result;
        } catch (Exception pe) {
            return null;
        }
    }

    /**
     * @param strDate
     * @param pattern
     * @return
     */
    public static final Date parseDate(String strDate, String pattern) {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(pattern);
        try {
            date = df.parse(strDate);
            return date;
        } catch (Exception pe) {
            return null;
        }
    }

    /**
     * @return formated date by yyyy-MM-dd
     */
    public static final String formatDate(Date date) {
        if (date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * @return formated time by HH:mm:ss
     */
    public static final <T extends Date> String formatTime(T date) {
        if (date == null) return null;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(date);
    }

    /**
     * @param date
     * @return formated time by yyyy-MM-dd HH:mm:ss
     */
    public static final <T extends Date> String formatTimestamp(T date) {
        if (date == null) return null;
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return timestampFormat.format(date);
    }

    public static String formatTimestamp(long timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String result = simpleDateFormat.format(timestamp);
        return result;
    }

    public static final String formatTimestamp(Long mills) {
        return formatTimestamp(new Date(mills));
    }

    /**
     * @param date
     * @param pattern: Date format pattern
     * @return
     */
    public static final <T extends Date> String format(T date, String pattern) {
        if (date == null) return null;
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            String result = df.format(date);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param original
     * @param days
     * @param hours
     * @param minutes
     * @param seconds
     * @return original+day+hour+minutes+seconds+millseconds
     */
    public static final <T extends Date> T addTime(T original, int days, int hours, int minutes, int seconds) {
        if (original == null) return null;
        long newTime = original.getTime() + m_day * days + m_hour * hours + m_minute * minutes + m_second * seconds;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addDay(T original, int days) {
        if (original == null) return null;
        long newTime = original.getTime() + m_day * days;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addHour(T original, int hours) {
        if (original == null) return null;
        long newTime = original.getTime() + m_hour * hours;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addMinute(T original, int minutes) {
        if (original == null) return null;
        long newTime = original.getTime() + m_minute * minutes;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    public static final <T extends Date> T addSecond(T original, int second) {
        if (original == null) return null;
        long newTime = original.getTime() + m_second * second;
        T another = (T) original.clone();
        another.setTime(newTime);
        return another;
    }

    /**
     * @param day
     * @return for example ,1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
     */
    public static final <T extends Date> T getBeginningTimeOfDay(T day) {
        if (day == null) return null;
        //new Date(0)=Thu Jan 01 08:00:00 CST 1970
        String strDate = formatDate(day);
        Long mill = parseDate(strDate).getTime();
        T another = (T) day.clone();
        another.setTime(mill);
        return another;
    }

    /**
     * @param day
     * @return for example ,1997/01/02 22:03:00,return 1997/01/02 23:59:59.999
     */
    public static final <T extends Date> T getLastTimeOfDay(T day) {
        if (day == null) return null;
        Long mill = getBeginningTimeOfDay(day).getTime() + m_day - 1;
        T another = (T) day.clone();
        another.setTime(mill);
        return another;
    }

    /**
     * 09:00:00,09:07:00 ---> 9:00,9:7:00
     *
     * @param time
     * @return
     */
    public static final String formatTime(String time) {
        if (time == null) return null;
        time = time.trim();
        if (StringUtils.isBlank(time)) throw new IllegalArgumentException("时间格式有错误！");
        time = time.replace('：', ':');
        String[] times = time.split(":");
        String result = "";
        if (times[0].length() < 2) result += "0" + times[0] + ":";
        else result += times[0] + ":";
        if (times.length > 1) {
            if (times[1].length() < 2) result += "0" + times[1];
            else result += times[1];
        } else {
            result += "00";
        }
        Timestamp.valueOf("2001-01-01 " + result + ":00");
        return result;
    }

    public static boolean isTomorrow(Date date) {
        if (date == null) return false;
        if (formatDate(addTime(new Date(DateUtil.GEWARA_TIME), 1, 0, 0, 0)).equals(formatDate(date)))
            return true;
        return false;
    }

    /***
     * @param date
     * @return 1, 2, 3, 4, 5, 6, 7
     */
    private static int[] chweek = new int[]{0, 7, 1, 2, 3, 4, 5, 6};

    /**
     * @param date
     * @return 1, 2, 3, 4, 5, 6, 7
     */
    public static Integer getWeek(Date date) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(DateUtil.timeMillis());
        cal.setTime(date);
        return chweek[cal.get(Calendar.DAY_OF_WEEK)];
    }

    private static String[] cnweek = new String[]{"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private static String[] cnSimpleweek = new String[]{"", "日", "一", "二", "三", "四", "五", "六"};

    /**
     * @param date
     * @return "周日", "周一", "周二", "周三", "周四", "周五", "周六"
     */
    public static String getCnWeek(Date date) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(DateUtil.timeMillis());
        cal.setTime(date);
        return cnweek[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public static String getCnSimpleWeek(Date date) {
        if (date == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(DateUtil.timeMillis());
        cal.setTime(date);
        return cnSimpleweek[cal.get(Calendar.DAY_OF_WEEK)];
    }

    public static Integer getMonth(Date date) {
        if (date == null) return null;
        String month = format(date, "M");
        return Integer.parseInt(month);
    }

    public static Integer getCurrentDay() {
        return getDay(new Date(DateUtil.GEWARA_TIME));
    }

    public static Integer getCurrentMonth() {
        return getMonth(new Date(DateUtil.GEWARA_TIME));
    }

    public static Integer getCurrentYear() {
        return getYear(new Date(DateUtil.GEWARA_TIME));
    }

    public static Integer getYear(Date date) {
        if (date == null) return null;
        String year = DateUtil.format(date, "yyyy");
        return Integer.parseInt(year);
    }

    public static Integer getDay(Date date) {
        if (date == null) return null;
        String year = DateUtil.format(date, "d");
        return Integer.parseInt(year);
    }

    public static String getCurDateStr() {
        return DateUtil.formatDate(new Date(DateUtil.GEWARA_TIME));
    }

    public static String getCurTimeStr() {
        return DateUtil.formatTimestamp(new Date(DateUtil.GEWARA_TIME));
    }

    public static boolean isAfter(Date date) {
        if (date.after(new Date(DateUtil.GEWARA_TIME))) {
            return true;
        }
        return false;
    }

    /**
     * 获取date所在月份的星期为weektype且日期在date之后（或等于）的所有日期
     *
     * @param weektype
     * @return
     */
    public static List<Date> getWeekDateList(Date date, String weektype) {
        int curMonth = getMonth(date);
        int week = Integer.parseInt(weektype);
        int curWeek = getWeek(date);
        int sub = (7 + week - curWeek) % 7;
        Date next = addDay(date, sub);
        List<Date> result = new ArrayList<Date>();
        while (getMonth(next) == curMonth) {
            result.add(next);
            next = addDay(next, 7);
        }
        return result;
    }

    /**
     * 获取date之后(包括date)的num个星期为weektype日期（不限制月份）
     *
     * @param weektype
     * @return
     */
    public static List<Date> getWeekDateList(Date date, String weektype, int num) {
        int week = Integer.parseInt(weektype);
        int curWeek = getWeek(date);
        List<Date> result = new ArrayList<Date>();
        int sub = (7 + week - curWeek) % 7;
        Date next = addDay(date, sub);
        for (int i = 0; i < num; i++) {
            result.add(next);
            next = addDay(next, 7);
        }
        return result;
    }

    /**
     * 获取date所在星期的周一至周日的日期
     *
     * @param date
     * @return
     */
    public static List<Date> getCurWeekDateList(Date date) {
        int curWeek = getWeek(date);
        List<Date> dateList = new ArrayList<Date>();
        for (int i = 1; i <= 7; i++) dateList.add(DateUtil.addDay(date, -curWeek + i));
        return dateList;
    }

    public static Date getCurDate() {
        return getBeginningTimeOfDay(new Date(DateUtil.GEWARA_TIME));
    }

    /**
     * 获取日期所在月份的第一天
     *
     * @param date
     * @return
     */
    public static Date getMonthFirstDay(Date date) {
        String dateStr = format(date, "yyyy-MM") + "-01";
        return parseDate(dateStr);
    }

    /**
     * 获取日期所在月份的最后一天
     *
     * @param date
     * @return
     */
    public static Date getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(DateUtil.timeMillis());
        cal.setTime(date);
        String dateStr = format(date, "yyyy-MM") + "-" + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return parseDate(dateStr);
    }

    public static String formatDate(int days) {
        return formatDate(addDay(new Date(DateUtil.GEWARA_TIME), days));
    }

    public static Timestamp getCurTimestamp() {
        return getBeginningTimeOfDay(new Timestamp(System.currentTimeMillis()));
    }

    public static Integer getHour(Date date) {
        if (date == null) return null;
        String hour = format(date, "H");
        return Integer.parseInt(hour);
    }

    public static String getTimeDesc(Timestamp time) {
        if (time == null) return "";
        return getDateDesc(time.getTime());
    }

    public static String getDateDesc(Date time) {
        if (time == null) return "";
        return getDateDesc(time.getTime());
    }

    public static String getDateDesc(long time) {
        String timeContent = "";
        Long ss = GEWARA_TIME - time;
        if (ss > 10000) {
            Long minute = ss / 60000;
            if (minute >= 60) {
                Long hour = minute / 60;
                if (hour >= 24) {
                    Long day = hour / 24;
                    if (day >= 30) {
                        Long month = day / 30;
                        if (month >= 12) {
                            timeContent = (month / 12) + "年前";
                        } else {
                            timeContent = month + "月前";
                        }
                    } else {
                        timeContent = day + "天前";
                    }
                } else {
                    timeContent = hour + "小时前";
                }
            } else {
                if (minute < 1) {
                    minute = 1L;
                }
                timeContent = minute + "分钟前";
            }
        } else {
            timeContent = "刚刚";
        }
        return timeContent;
    }

    /**
     * author: bob
     * date:	20100729
     * 截取日期, 去掉年份
     * param: 	date1
     * eg. 传入"1986-07-28", 返回 07-28
     */
    public static String getMonthAndDay(Date date) {
        return formatDate(date).substring(5);
    }

    public static Date getMillDate() {
        return new Date(DateUtil.GEWARA_TIME);
    }

    public static Timestamp getMillTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 时间差：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> String getDiffStr(T day1, T day2) {
        if (day1 == null || day2 == null) return "---";
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        long hour = diff / 3600;
        long minu = diff % 3600 / 60;
        long second = diff % 60;
        return (sign < 0 ? "-" : "+") + (hour == 0 ? "" : hour + "小时") + (minu == 0 ? "" : minu + "分") + (second == 0 ? "" : second + "秒");
    }

    /**
     * 时间差（秒）：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> long getDiffSecond(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return sign * diff;
    }

    /**
     * 时间差（分钟）：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> double getDiffMinu(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return Math.round(diff * 1.0d * 10 / 6.0) / 100.0 * sign;//两位小数
    }

    /**
     * 时间差（小时）：day1-day2
     *
     * @param day1
     * @param day2
     * @return
     */
    public static final <T extends Date> double getDiffHour(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        long sign = diff / Math.abs(diff);
        diff = Math.abs(diff) / 1000;
        return Math.round(diff * 1.0d / 3.6) / 1000.0 * sign;//三位小数
    }

    public static final <T extends Date> int getDiffDay(T day1, T day2) {
        if (day1 == null || day2 == null) return 0;
        long diff = day1.getTime() - day2.getTime();
        diff = Math.abs(diff) / 1000;
        return Math.round(diff / (3600 * 24));
    }

    public static boolean isAfterOneHour(Date date, String time) {
        String datetime = formatDate(date) + " " + time + ":00";
        if (addHour(parseTimestamp(datetime), -1).after(getMillTimestamp())) {
            return true;
        }
        return false;
    }

    public static boolean isValidDate(String fyrq) {
        return DateUtil.parseDate(fyrq) != null;
    }

    public static final <T extends Date> String getAgendaDate(Date t, String pattern) {
        if (t == null) return "";
        try {
            Long paramsTimes = getBeginningTimeOfDay(t).getTime();
            if (getCurDate().getTime() == paramsTimes) return "今天";
            if (addDay(getCurDate(), 1).getTime() == paramsTimes) return "明天";
            if (addDay(getCurDate(), 2).getTime() == paramsTimes) return "后天";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(t);

        } catch (Exception e) {
            return "";
        }
    }

    public static final <T extends Date> String getAgendaDate2(Date t) {
        if (t == null) return "";
        try {
            Long paramsTimes = getBeginningTimeOfDay(t).getTime();
            if (getCurDate().getTime() == paramsTimes) return "今天";
            if (addDay(getCurDate(), 1).getTime() == paramsTimes) return "明天";
            if (addDay(getCurDate(), 2).getTime() == paramsTimes) return "后天";

            return "三天后";
        } catch (Exception e) {
            return "";
        }
    }

    public static long getCurDateMills(Date date) {
        if (date == null) return 0;
        return date.getTime();
    }

    /**
     * eg.  1997/01/02 22:03:00,return 1997/01/02 00:00:00.0
     **/
    public static Timestamp getBeginTimestamp(Date date) {
        return new Timestamp(getBeginningTimeOfDay(date).getTime());
    }

    public static Timestamp getEndTimestamp(Date date) {
        return new Timestamp(getLastTimeOfDay(date).getTime());
    }

    public static Date getDateFromTimestamp(Timestamp timestamp) {
        return new Date(getCurDateMills(timestamp));
    }

    public static int after(Date date1, Date date2) {
        date1 = getBeginningTimeOfDay(date1);
        date2 = getBeginningTimeOfDay(date2);
        return date1.compareTo(date2);
    }

    public static Timestamp mill2Timestamp(Long mill) {
        if (mill == null) return null;
        return new Timestamp(mill);
    }

    /**
     * 将yyyy-mm-dd格式化为xx月xx日
     *
     * @param dateStr
     * @return
     */
    public static String formatChinaStyle(String dateStr) {
        return dateStr.substring(5).replace("-", "月") + "日";
    }

    /**
     * 将yyyy-mm-dd格式化为xxxx年xx月xx日
     *
     * @param dateStr
     * @return 1983年2月22日
     */
    public static String formatDate(String dateStr) {
        int[] date = new int[3];
        if (StringUtils.isNotEmpty(dateStr)) {
            String str[] = dateStr.split("-");
            date[0] = Integer.valueOf(str[0]);
            date[1] = Integer.valueOf(str[1]);
            date[2] = Integer.valueOf(str[2]);
        }
        return date[0] + "年" + date[1] + "月" + date[2] + "日";
    }

    /**
     * 将yyyy-mm-dd hh-mm格式化为xxxx年xx月xx日
     *
     * @param dateStr
     * @return 1983年2月22日
     */
    public static String formatDate2(String dateStr) {
        int[] date = new int[3];
        if (StringUtils.isNotEmpty(dateStr)) {
            String str[] = dateStr.split("-");
            date[0] = Integer.valueOf(str[0]);
            date[1] = Integer.valueOf(str[1]);
            date[2] = Integer.valueOf(str[2].substring(0, 2));
        }
        return date[0] + "年" + date[1] + "月" + date[2] + "日";
    }

    /**
     * 将yyyy-mm-dd hh-mm格式化为xxxx年xx月xx日
     *
     * @param dateStr
     * @return 1983年2月22日 12:20:00
     */
    public static String formatDate3(String dateStr) {
        int[] date = new int[3];
        String[] time = new String[2];
        if (StringUtils.isNotEmpty(dateStr)) {
            String str[] = dateStr.split("-");
            date[0] = Integer.valueOf(str[0]);
            date[1] = Integer.valueOf(str[1]);
            date[2] = Integer.valueOf(str[2].substring(0, 2));
        }

        if (StringUtils.isNotEmpty(dateStr)) {
            String str[] = dateStr.split(" ");
            time[0] = str[0];
            time[1] = str[1];
        }
        return date[0] + "年" + date[1] + "月" + date[2] + "日" + " " + time[1];
    }


    /*
     * 几天后结束
     */
    public static final String ISCOMPLETE = "已结束";

    public static String[] TimeLeftFromNowOn(Date date) {
        String ret[] = new String[2];
        ret[0] = ISCOMPLETE;
        ret[1] = "";
        if (date == null) {
            return ret;
        }

        long NowTime = GEWARA_TIME;
        long dateLimit = date.getTime();
        if (dateLimit > NowTime) {
            long TimeLeft = dateLimit - NowTime;
            long day = TimeLeft / (1000 * 60 * 60 * 24) + 1;
            if (getAgendaDate2(date).equals("今天")) {
                ret[0] = "今天";
                ret[1] = "结束";
            } else if (getAgendaDate2(date).equals("明天")) {
                ret[0] = "明天";
                ret[1] = "结束";
            } else if (getAgendaDate2(date).equals("后天")) {
                ret[0] = "后天";
                ret[1] = "结束";
            } else {
                ret[0] = "" + day;
                ret[1] = "天后结束";
            }
            return ret;
        } else {
            return ret;
        }
    }

    public static String[] TimeFromNowOn(Date startdate, Date endDate) {
        String ret[] = new String[2];
        ret[0] = ISCOMPLETE;
        ret[1] = "";
        long NowTime = GEWARA_TIME;
        if (startdate != null) {
            long startDateLimit = startdate.getTime();
            if (startDateLimit > NowTime) {
                long TimeLeft = startDateLimit - NowTime;
                long day = TimeLeft / (1000 * 60 * 60 * 24);
                if (day <= 2) {
                    day += 1;
                }
                if (getAgendaDate2(startdate).equals("今天")) {
                    ret[0] = "今天";
                    ret[1] = "开始";
                } else if (getAgendaDate2(startdate).equals("明天")) {
                    ret[0] = "明天";
                    ret[1] = "开始";
                } else if (getAgendaDate2(startdate).equals("后天")) {
                    ret[0] = "后天";
                    ret[1] = "开始";
                } else {
                    ret[0] = "" + day;
                    ret[1] = "天后开始";
                }
                return ret;
            } else {
                if (endDate != null) {
                    long endDateLimit = endDate.getTime();
                    if (endDateLimit > NowTime) {
                        long TimeLeft = endDateLimit - NowTime;
                        long day = TimeLeft / (1000 * 60 * 60 * 24) + 1;
                        if (getAgendaDate2(endDate).equals("今天")) {
                            ret[0] = "今天";
                            ret[1] = "结束";
                        } else if (getAgendaDate2(endDate).equals("明天")) {
                            ret[0] = "明天";
                            ret[1] = "结束";
                        } else if (getAgendaDate2(endDate).equals("后天")) {
                            ret[0] = "后天";
                            ret[1] = "结束";
                        } else {
                            ret[0] = "" + day;
                            ret[1] = "天后结束";
                        }
                        return ret;
                    }
                }
            }
        } else if (endDate != null) {
            long endDateLimit = endDate.getTime();
            if (endDateLimit > NowTime) {
                long TimeLeft = endDateLimit - NowTime;
                long day = TimeLeft / (1000 * 60 * 60 * 24) + 1;
                if (getAgendaDate2(endDate).equals("今天")) {
                    ret[0] = "今天";
                    ret[1] = "结束";
                } else if (getAgendaDate2(endDate).equals("明天")) {
                    ret[0] = "明天";
                    ret[1] = "结束";
                } else if (getAgendaDate2(endDate).equals("后天")) {
                    ret[0] = "后天";
                    ret[1] = "结束";
                } else {
                    ret[0] = "" + day;
                    ret[1] = "天后结束";
                }
                return ret;
            }
        }
        return ret;
    }

}
