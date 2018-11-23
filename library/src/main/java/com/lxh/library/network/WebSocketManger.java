package com.lxh.library.network;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.lxh.library.uitils.LogUtils;
import okhttp3.*;
import okio.ByteString;

import java.util.Vector;

public class WebSocketManger {
    private static final WebSocketManger ourInstance = new WebSocketManger();

    public static WebSocketManger getInstance() {
        return ourInstance;
    }

    private WebSocket webSocket;
    //连接次数
    private int connectCount;

    private String url;

    private Vector<WebSocketListener> socketListeners;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            webSocketConnect();
        }
    };

    private Runnable heatRunnable = new Runnable() {
        @Override
        public void run() {
            if (null != webSocket) {
                webSocket.send(" ");
                handler.postDelayed(heatRunnable, 30 * 1000);
            }
        }
    };

    private WebSocketManger() {
    }

    public WebSocketManger initUrl(String url){
        this.url = url;
        return this;
    }

    /**
     * 初始化连接socket
     */
    public WebSocketManger webSocketConnect() throws IllegalArgumentException {
        if (TextUtils.isEmpty(url)) throw new IllegalArgumentException("url不能为空");
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        ClientWebSocketListener listener = new ClientWebSocketListener();
        mOkHttpClient.newWebSocket(request, listener);
        mOkHttpClient.dispatcher().executorService().shutdown();
        return this;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    private void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
        if (webSocket == null) {
            resetSocket();
        } else {
            handler.postDelayed(heatRunnable, 30 * 1000);
        }
    }

    /**
     * 从新连接间隔第一次10s 之后每次加10s 最多50s
     */
    private void resetSocket() {
        int count = 5;
        if (connectCount >= 5) count = connectCount;
        long delayMills = count * 10 * 1000;
        connectCount++;
        handler.postDelayed(runnable, delayMills);
    }

    public void addSocketListener(WebSocketListener socketListener) {
        if (this.socketListeners == null) this.socketListeners = new Vector<>();
        if (!this.socketListeners.contains(socketListener))
            this.socketListeners.addElement(socketListener);
    }

    public void removeSocketListener(WebSocketListener socketListener) {
        if (this.socketListeners != null) this.socketListeners.removeElement(socketListener);
    }

    public void removeAllSocketListener() {
        if (this.socketListeners != null) this.socketListeners.removeAllElements();
    }

    private class ClientWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("我是客户端");
            //连接成功后重置
            connectCount = 1;
            setWebSocket(webSocket);
            if (null != socketListeners) {
                for (WebSocketListener socketListener : socketListeners) {
                    socketListener.onOpen(webSocket, response);
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            LogUtils.INSTANCE.e(text);
            if (null != socketListeners) {
                for (WebSocketListener socketListener : socketListeners) {
                    socketListener.onMessage(webSocket, text);
                }
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            if (null != socketListeners) {
                for (WebSocketListener socketListener : socketListeners) {
                    socketListener.onMessage(webSocket, bytes);
                }
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            LogUtils.INSTANCE.e("response--" + t.getMessage());
            setWebSocket(null);
            if (null != socketListeners) {
                for (WebSocketListener socketListener : socketListeners) {
                    socketListener.onFailure(webSocket, t, response);
                }
            }
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            LogUtils.INSTANCE.e("reason--" + reason);
            getInstance().webSocket = null;
            if (null != socketListeners) {
                for (WebSocketListener socketListener : socketListeners) {
                    socketListener.onClosing(webSocket, code, reason);
                }
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            LogUtils.INSTANCE.e("onClosed--" + reason);
            getInstance().webSocket = null;
            if (null != socketListeners) {
                for (WebSocketListener socketListener : socketListeners) {
                    socketListener.onClosed(webSocket, code, reason);
                }
            }
        }
    }
}
