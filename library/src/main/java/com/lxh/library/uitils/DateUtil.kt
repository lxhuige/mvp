package com.lxh.library.uitils

import com.lxh.library.AppManager
import com.lxh.library.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Created by lxh on 2018/3/2.
 */
object DateUtil {

    /**
     * 根据年 月 获取对应的月份 天数
     */
    fun getDaysByYearMonth(year: Int, month: Int): Int {
        val a = Calendar.getInstance()
        a.set(Calendar.YEAR, year)
        a.set(Calendar.MONTH, month - 1)
        a.set(Calendar.DATE, 1)
        a.roll(Calendar.DATE, -1)
        return a.get(Calendar.DATE)
    }

    /**
     * 获取当月的 天数
     */
    fun getCurrentMonthDay(): Int {
        val a = Calendar.getInstance()
        a.set(Calendar.DATE, 1)
        a.roll(Calendar.DATE, -1)
        return a.get(Calendar.DATE)
    }


    /**
     * 与当前时间比较早晚
     *
     * @param time 需要比较的时间
     * @return 输入的时间比现在时间晚则返回true
     */
    fun compareNowTime(time: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
        try {
            val parse = dateFormat.parse(time)
            val parse1 = dateFormat.parse(getNowTime())
            val diff = parse1.time - parse.time
            return diff <= 0
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    fun timeToMillisecond(time: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
        return dateFormat.parse(time).time
    }

    /**
     * 获取当前时间
     *
     * @return 2018-03-06 9:30
     */
    fun getNowTime(): String {
        val timeString: String?
        val year = getNowYear()
        val month = thanTen(getNowMonth().toInt())
        val monthDay = thanTen(getNowDay().toInt())
        val hour = thanTen(getNowHour().toInt())
        val minute = thanTen(getMinute().toInt())
        val second = thanTen(getSecond().toInt())

        timeString = (year + "-" + month + "-" + monthDay + " " + hour + ":"
                + minute + ":" + second)
        return timeString
    }

    /**
     * 获取当前时间
     *
     * @return 2018-03-06 9:30
     */
    fun getTime(): String {
        val curDate = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE)
        return dateFormat.format(curDate)
    }

    fun dateToString(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE)
        return dateFormat.format(date)
    }
    fun dateToString(date: Date,format:String): String {
        val dateFormat = SimpleDateFormat(format, Locale.SIMPLIFIED_CHINESE)
        return dateFormat.format(date)
    }

    /**
     * 获取当前时间
     *
     * @return 2018-03-06
     */
    fun getNowYearMonthDay(): String {
        val year = getNowYear()
        val month = getNowMonth()
        val monthDay = getNowDay()
        return "$year-$month-$monthDay"
    }

    /**
     * 十一下加零
     *
     * @param str
     * @return
     */
    private fun thanTen(str: Int): String {
        return if (str < 10) "0$str" else str.toString()
    }

    private fun getNowYear(): String {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.YEAR).toString()
    }

    private fun getNowMonth(): String {
        val cal = Calendar.getInstance()
        return (cal.get(Calendar.MONTH) + 1).toString()
    }

    private fun getNowDay(): String {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.DAY_OF_MONTH).toString()
    }

    private fun getNowHour(): String {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.HOUR_OF_DAY).toString()
    }

    private fun getMinute(): String {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.MINUTE).toString()
    }

    private fun getSecond(): String {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.SECOND).toString()
    }


    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    fun getChatTimeStr(timeStamp: Long): String {
        if (timeStamp == 0L) return ""
        val activity = AppManager.currentActivity()
        val inputTime = Calendar.getInstance()
        inputTime.timeInMillis = timeStamp * 1000
        val currenTimeZone = inputTime.time
        val calendar = Calendar.getInstance()
        if (!calendar.after(inputTime)) {
            //当前时间在输入时间之前
            val sdf = SimpleDateFormat("yyyy" + activity.resources.getString(R.string.time_year) + "MM" + activity.resources.getString(R.string.time_month) + "dd" + activity.resources.getString(R.string.time_day), Locale.SIMPLIFIED_CHINESE)
            return sdf.format(currenTimeZone)
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE)
            return sdf.format(currenTimeZone)
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        if (calendar.before(inputTime)) {
            val sdf = SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE)
            return activity.resources.getString(R.string.time_yesterday) + " " + sdf.format(currenTimeZone)
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.MONTH, Calendar.JANUARY)
            if (calendar.before(inputTime)) {
                val sdf = SimpleDateFormat("M" + activity.resources.getString(R.string.time_month) + "d" + activity.resources.getString(R.string.time_day) + " HH:mm", Locale.SIMPLIFIED_CHINESE)
                return sdf.format(currenTimeZone)
            } else {
                val sdf = SimpleDateFormat("yyyy" + activity.resources.getString(R.string.time_year) + "MM" + activity.resources.getString(R.string.time_month) + "dd" + activity.resources.getString(R.string.time_day) + " HH:mm", Locale.SIMPLIFIED_CHINESE)
                return sdf.format(currenTimeZone)
            }

        }

    }

}