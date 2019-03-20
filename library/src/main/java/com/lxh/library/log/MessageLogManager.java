package com.lxh.library.log;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageLogManager {
    private static final MessageLogManager ourInstance = new MessageLogManager();

    public static MessageLogManager getInstance() {
        return ourInstance;
    }

    private MessageLogManager() {
    }

    private final static String fileName = "log.txt";

    /**
     * @param content 将字符串写入到文本文件中
     */
    synchronized void writeTxtToFile(String content, Context context) {
        String filePath = getFilePath(context);
//生成文件夹之后，再生成文件，不然会出错
        try {
            makeFilePath(filePath + File.separator);
            String strFilePath = filePath + File.separator + fileName;
            // 每次写入时，都换行写
            String strContent = content + "---" + getTime() + "\r\n";
            File file = new File(strFilePath);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e.getMessage());
        }
    }

    private String getFilePath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getCodeCacheDir().getPath();
        }
        return context.getCacheDir().getPath();
//        return Environment.getExternalStorageDirectory().getPath();
    }

    // 生成文件
    private void makeFilePath(String filePath) throws IOException {
        makeRootDirectory(filePath);
        File file = new File(filePath + fileName);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
        }
    }

    // 生成文件夹
    private void makeRootDirectory(String filePath) {
        File file;
        file = new File(filePath);
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
        }
    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * @return true 需要上传日志
     */
    boolean isUpdate(Context context) {
        File file = getFile(context);
        return file.exists() && file.length() > 10;
    }

    boolean deleteFile(Context context) {
        File file = getFile(context);
        return file.delete();
    }

    File getFile(Context context) {
        String filePath = getFilePath(context);
        String s = filePath + File.separator + fileName;
        return new File(s);
    }
}
