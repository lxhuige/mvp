package com.lxh.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * This class is for base class of WebView widget.
 * Created by s1823 on 2016/4/7.
 */
public class JWebView extends WebView implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Views.onItemClickPosListener posListener;//进度回调

    public void setPosListener(Views.onItemClickPosListener posListener) {
        this.posListener = posListener;
    }

    public JWebView(Context context) {
        super(context);
    }

    public JWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setBuiltInZoomControls(false);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(false);
        getSettings().setBlockNetworkImage(false);
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        getSettings().setDomStorageEnabled(true);
        getSettings().setDatabaseEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setPluginState(WebSettings.PluginState.ON);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH); // 提高渲染的优先级
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (null != mSwipeRefreshLayout) {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = getTitle().trim().toLowerCase();
                if (TextUtils.isEmpty(title) || title.contains("404") || title.contains("502") || title.contains("error")) {
                    if (null != posListener) {
                        posListener.onItemClickPos(404);
                    }
                } else {
                    if (null != posListener) {
                        posListener.onItemClickPos(200);
                    }
                }
                setSwipeRefreshLoadedState();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                setSwipeRefreshLoadedState();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                setSwipeRefreshLoadedState();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受所有网站的证书
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (null != posListener) {
                    posListener.onItemClickPos(newProgress);
                }
            }

        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        setLongClickable(false);
    }

    @Override
    public void onRefresh() {
        setSwipeRefreshLoadingState();
        reload();
    }

    private void setSwipeRefreshLoadingState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    private void setSwipeRefreshLoadedState() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }
}
