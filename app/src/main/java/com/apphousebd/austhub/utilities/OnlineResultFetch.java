package com.apphousebd.austhub.utilities;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Asif Imtiaz Shaafi on 2016.
 * Email: a15Shaafi.209@gmail.com
 */

public class OnlineResultFetch {

    public static final String LAB_RESULT_BASE_URL = "http://www.aust.edu/result/";
    public static final String THEORY_RESULT_BASE_URL = "http://www.aust.edu/result/";

    public static void loadUrl(final Context context,
                               final String url,
                               final WebView webView,
                               final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setRefreshing(true);

        webView.clearCache(true);

        WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebChromeClient(new WebChromeClient());

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.setFitsSystemWindows(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        if (NetworkConnectionManager.hasNetworkConnection(context)) {
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

//                    webView.loadUrl("file:///android_asset/Error.html");
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    swipeRefreshLayout.setRefreshing(false);
                    return false;
                }
            });

        } else {
            webView.loadUrl("file:///android_asset/Error.html");
        }


        swipeRefreshLayout.setRefreshing(false);

    }

}
