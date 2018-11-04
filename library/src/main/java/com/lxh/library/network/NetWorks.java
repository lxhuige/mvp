package com.lxh.library.network;


import com.lxh.library.uitils.LogUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author lixiaohui 2017/8/19.
 */

public class NetWorks {

    private static final int MAX_TIMEOUT = 60 * 3;
    private static Retrofit Host_Retrofit = null;
    private static String baseUrl;

    public static void setBaseUrl(String baseUrl) {
        NetWorks.baseUrl = baseUrl;
    }

    private static synchronized void init() {
        if (null == Host_Retrofit) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(MAX_TIMEOUT, TimeUnit.MINUTES);
            builder.readTimeout(MAX_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(MAX_TIMEOUT, TimeUnit.SECONDS);
            if (baseUrl.startsWith("https")) {
                getCetFactory(builder);
                builder.hostnameVerifier(new UnSafeHostnameVerifier());//添加hostName验证器
            }
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json; charset=UTF-8")
                            .addHeader("Connection", "keep-alive")
                            .build();
                    return chain.proceed(request);
                }
            });
            OkHttpClient build = builder.build();
            Host_Retrofit = new Retrofit.Builder()
                    .client(build)
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJavaCallAdapterFactory.create()
                    .build();
        }
    }

    public static Retrofit getHostRetrofit() {
        init();
        return Host_Retrofit;
    }

    private static class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;//自行添加判断逻辑，true->Safe，false->unsafe
        }
    }

    private static SSLSocketFactory getCetFactory(OkHttpClient.Builder builder) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), new MytmArray());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class MytmArray implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] X509Certificates, String authType) throws CertificateException {
            if (X509Certificates == null) {
                LogUtils.INSTANCE.e("TAG", "check server X509Certificates is null");
                throw new IllegalArgumentException("check server X509Certificates is null");
            }
            if (X509Certificates.length == 0) {
                LogUtils.INSTANCE.e("TAG", "check server X509Certificates is empty");
                throw new IllegalArgumentException("check server X509Certificates is empty");
            }
            for (X509Certificate cert : X509Certificates) {
                cert.checkValidity();
            }
        }
    }

}
