package com.example.clicknship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class Catalog extends AppCompatActivity {

    private WebView Catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Button searchBtn = findViewById(R.id.searchB);
        TextView item =  findViewById(R.id.search);

        Catalog = (WebView) findViewById(R.id.webview);
        final String searchURL = "https://www.amazon.com/s/?field-keywords=";
        // Here is I make transparent background for WebView
        Catalog.setBackgroundColor(0x00000000);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Catalog.setWebViewClient(new WebViewClient());
                Catalog.loadUrl(searchURL + item.getText().toString());

                WebSettings webSettings = Catalog.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            }
        });

        Catalog.setWebViewClient(new WebViewClient());
        Catalog.loadUrl(searchURL);

        WebSettings webSettings = Catalog.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    public void onBackPressed() {
        if (Catalog.canGoBack()) {
            Catalog.goBack();
        } else {
            super.onBackPressed();
        }
    }
}