package com.lxh.library.network;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManger {
    private static ExecutorService executor = Executors.newCachedThreadPool();
    private long fileTotal;
    private long current;
    private volatile boolean isc;
    private Handler handler = new Handler(Looper.getMainLooper());

    private void downloadS(final String url, final File file, final CallBack callBack) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                    urlConnection.setConnectTimeout(30 * 1000);
                    urlConnection.setReadTimeout(30 * 1000);
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestMethod("GET");
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == 200) {
                        fileTotal = urlConnection.getContentLength();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        InputStream inputStream = url1.openStream();
                        byte[] b = new byte[1024];
                        int read;
                        while ((read = inputStream.read(b)) != -1) {
                            fileOutputStream.write(b, 0, read);
                            current += read;
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        inputStream.close();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                isc = false;
                                if (null != callBack) callBack.success(file);
                            }
                        });
                    }
                    urlConnection.disconnect();
                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callBack) callBack.fail(e);
                        }
                    });
                }
            }
        };
        executor.execute(runnable);
    }

    public static void download(final String url, final File file, final CallBack callBack) {
        final DownloadManger downloadManger = new DownloadManger();
        downloadManger.downloadS(url, file, callBack);
        downloadManger.isc = true;
        downloadManger.handler.post(new Runnable() {
            @Override
            public void run() {
                if (null != callBack)
                    callBack.doingLoad(downloadManger.fileTotal, downloadManger.current);
                if (downloadManger.isc)
                    downloadManger.handler.postDelayed(this, 1000);
            }
        });
    }

    public interface CallBack {
        void success(File file);

        void fail(Exception e);

        void doingLoad(long fileTotal, long current);
    }

}
