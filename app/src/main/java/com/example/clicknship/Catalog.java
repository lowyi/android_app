package com.example.clicknship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
                if (!searchURL.startsWith("file://")) {
                    if (!searchURL.startsWith("javascript://")) {
                        if (!searchURL.startsWith("content://")) {
                            Catalog.loadUrl(searchURL + item.getText().toString());
                        }else{
                            new AlertDialog.Builder(Catalog.this)
                                    .setTitle("Illegal Website Access")
                                    .setMessage("You Have Access An Illegal website To protect your data safety, you are not allowed to use this app.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            finishAffinity();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    }else{
                        new AlertDialog.Builder(Catalog.this)
                                .setTitle("Illegal Website Access")
                                .setMessage("You Have Access An Illegal website To protect your data safety, you are not allowed to use this app.")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        finishAffinity();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }else{
                    new AlertDialog.Builder(Catalog.this)
                            .setTitle("Illegal Website Access")
                            .setMessage("You Have Access An Illegal website To protect your data safety, you are not allowed to use this app.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    finishAffinity();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                WebSettings webSettings = Catalog.getSettings();
                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            }
        });

        Catalog.setWebViewClient(new WebViewClient());
        if (!searchURL.startsWith("file://")) {
            if (!searchURL.startsWith("javascript://")) {
                if (!searchURL.startsWith("content://")) {
                    Catalog.loadUrl(searchURL);
                }else{
                    new AlertDialog.Builder(Catalog.this)
                            .setTitle("Illegal Website Access")
                            .setMessage("You Have Access An Illegal website To protect your data safety, you are not allowed to use this app.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    finishAffinity();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }else{
                new AlertDialog.Builder(Catalog.this)
                        .setTitle("Illegal Website Access")
                        .setMessage("You Have Access An Illegal website To protect your data safety, you are not allowed to use this app.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                finishAffinity();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }else{
            new AlertDialog.Builder(Catalog.this)
                    .setTitle("Illegal Website Access")
                    .setMessage("You Have Access An Illegal website To protect your data safety, you are not allowed to use this app.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finishAffinity();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        WebSettings webSettings = Catalog.getSettings();
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