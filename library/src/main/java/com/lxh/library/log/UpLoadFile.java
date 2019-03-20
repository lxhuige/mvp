package com.lxh.library.log;

import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UpLoadFile {
    private static final UpLoadFile ourInstance = new UpLoadFile();

    public static UpLoadFile getInstance() {
        return ourInstance;
    }

    private UpLoadFile() {
    }

     public void upLoadFile(final String uri, final File file) {
        if (CrashHandler.getInstance().isUpdate()) return;
        URL url;
        try {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setReadTimeout(30 * 1000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            // 设置编码格式
            urlConnection.setRequestProperty("Charset", "UTF-8");
            // 设置容许输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // 设置字符编码连接参数
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            urlConnection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
//            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Type","multipart/form-data; boundary="+boundary);
            OutputStream outputStream = urlConnection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//            dataOutputStream.writeBytes(twoHyphens + boundary + end);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=file;filename=log.text");
            dataOutputStream.writeBytes(end);
//            dataOutputStream.writeBytes("file=sjisjijs");

            FileInputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[1024];
            int count;
            while ((count = inputStream.read(b)) != -1) {
                dataOutputStream.write(b, 0, count);
            }
            dataOutputStream.writeBytes( boundary + end);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                CrashHandler.getInstance().deleteFile();
                urlConnection.disconnect();
            }
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 public    void upLoadFile(final String uri, final HashMap<String,String> param) {
//        if (CrashHandler.getInstance().isUpdate()) return;
        URL url;
        try {
            url = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(30 * 1000);
            urlConnection.setReadTimeout(30 * 1000);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            // 设置容许输出
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // 设置字符编码连接参数
            urlConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            urlConnection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
            urlConnection.setRequestProperty("Content-Type", "application/form-data");
            OutputStream outputStream = urlConnection.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            Set<String> keySet = param.keySet();
            for (String key : keySet) {
                dataOutputStream.write(key.getBytes());
                dataOutputStream.write("=".getBytes());
                String value = param.get(key);
                if (!TextUtils.isEmpty(value)){
                    assert value != null;
                    dataOutputStream.write(value.getBytes());
                }
                dataOutputStream.write("&".getBytes());
            }
            dataOutputStream.flush();
            dataOutputStream.close();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                CrashHandler.getInstance().deleteFile();
                urlConnection.disconnect();
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
