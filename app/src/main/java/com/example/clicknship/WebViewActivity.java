package com.example.clicknship;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView clickNShipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        clickNShipView = (WebView) findViewById(R.id.webview);
        final String searchURL = "https://www.amazon.com/s/?field-keywords=";
        // Here is I make transparent background for WebView
        clickNShipView.setBackgroundColor(0x00000000);

        clickNShipView.setWebViewClient(new WebViewClient());
        //clickNShipView.loadUrl("http://10.0.2.2:4200/catalogue");

        WebSettings webSettings = clickNShipView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    public void onBackPressed() {
        if (clickNShipView.canGoBack()) {
            clickNShipView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}