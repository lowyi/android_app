package com.example.clicknship;

import static com.example.clicknship.PasswordUtil.encryptStrAndToBase64;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Catalog extends AppCompatActivity {

    private WebView Catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Button searchBtn = findViewById(R.id.searchB);
        TextView item =  findViewById(R.id.search);

        Catalog = (WebView) findViewById(R.id.webview);
        final String searchURL = "http://192.168.1.71:8770/api/catalogueService/catalogue/catalogue/search?page=0&size=100";
        // Here is I make transparent background for WebView
        Catalog.setBackgroundColor(0x00000000);
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Catalog.setWebViewClient(new WebViewClient());
                if (!searchURL.startsWith("file://")) {
                    if (!searchURL.startsWith("javascript://")) {
                        if (!searchURL.startsWith("content://")) {
                            //Catalog.loadUrl(searchURL + item.getText().toString());
                            StringRequest postRequest = new StringRequest(Request.Method.POST, searchURL,
                                    new Response.Listener<String>()
                                    {
                                        @Override
                                        public void onResponse(String response) {
                                            // response
                                            String jsonStr = response;
                                            Toast.makeText(Catalog.this, "Catalog retrieve successful" + response, Toast.LENGTH_SHORT).show();

                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(Catalog.this, "Catalog retrieve unsuccessful" + error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            ) {
                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";
                                }

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("page", String.valueOf(0));
                                    params.put("size", String.valueOf(1));

                                    return params;
                                }

                                @Override
                                public byte[] getBody() {
                                    try {
                                        //input search condition here
                                        jsonBody.put("searchTerm", item.getText().toString());
                                        final String requestBody = jsonBody.toString();
                                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            };

                            queue.add(postRequest);

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