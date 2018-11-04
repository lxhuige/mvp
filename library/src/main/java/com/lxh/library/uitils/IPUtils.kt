package com.lxh.library.uitils

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object IPUtils {
    fun getIPAddress(context: Context): String? {
        val service = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        service?.let {
            if (service is ConnectivityManager){
                val info = service.activeNetworkInfo
                if (info != null && info.isConnected) {
                    if (info.type == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                        try {
                            val en = NetworkInterface.getNetworkInterfaces()
                            while (en.hasMoreElements()) {
                                val intf = en.nextElement()
                                val enumIpAddr = intf.inetAddresses
                                while (enumIpAddr.hasMoreElements()) {
                                    val inetAddress = enumIpAddr.nextElement()
                                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                        return inetAddress.getHostAddress()
                                    }
                                }
                            }
                        } catch (e: SocketException) {
                            e.printStackTrace()
                        }

                    } else if (info.type == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                       context.applicationContext.getSystemService(Context.WIFI_SERVICE)?.let {wifiManager->
                           if (wifiManager is WifiManager){
                               val wifiInfo = wifiManager.connectionInfo
                               return intIP2StringIP(wifiInfo.ipAddress)//得到IPV4地址
                           }
                        }
                    }
                }
            }
        }
        return null
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    private fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }


    fun getNetTime(): String? {
        val url: URL//取得资源对象
        try {
            url = URL("http://www.baidu.com")
            //            url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
            val uc = url.openConnection()//生成连接对象
            uc.connect() //发出连接
            val ld = uc.date //取得网站日期时间
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ld
            return formatter.format(calendar.time)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}