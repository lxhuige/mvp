package com.lxh.library.uitils

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * 正则表达式
 */
object RegularUtil {
    /**
     * 验证电话号码
     * @return true 电话号码正确 false 号码错误
     */
    fun isPhone(phone: String?): Boolean {
        if (TextUtils.isEmpty(phone)) return false
        val phonePattern = Pattern.compile("[1][34578][0-9]{9}")
        val m = phonePattern.matcher(phone)
        return m.matches()
    }

    /**
     * 校验身份证
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    fun isIDCard(idCard: String): Boolean {
        return Pattern.matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)", idCard)
    }

    /**
     * [0-9A-Za-z] {8,16} 由8-16位数字或这字母组成
     */
    fun isNumberOrLetter(content: String): Boolean {
        return Pattern.matches("^[0-9A-Za-z]{8,10}$", content)
    }

    /**
     * [0-9A-Za-z] {8,16} 由8-16位数字或这字母组成
     * @param least 最少几位
     * @param most 最多几位
     */
    fun isNumberOrLetter(content: String, least: Int, most: Int): Boolean {
        return Pattern.matches("^[0-9A-Za-z] { $least,$most }$", content)
    }

}