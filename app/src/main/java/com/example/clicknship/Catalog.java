package com.example.clicknship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Catalog extends AppCompatActivity {

    private WebView Catalog;

    private ListView listView;
    List<String> catalogList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        Button searchBtn = findViewById(R.id.searchB);
        TextView item =  findViewById(R.id.search);

        //Catalog = (WebView) findViewById(R.id.webview);
        final String searchURL = "https://www.amazon.com/s/?field-keywords=";
        // Here is I make transparent background for WebView
        Catalog.setBackgroundColor(0x00000000);

        // Display catalogue products
        catalogList = new ArrayList<>();
        listView = findViewById(R.id.catalogListView);

        getCatalogData getCatData = new getCatalogData();
        getCatData.execute();
        // End

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

    public class getCatalogData extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;

            try {
                System.out.println("Start of background execution.");
                URL url = new URL("http://10.0.2.2:8770/api/catalogueService/catalogue/catalogue/");
                urlConnection = (HttpURLConnection) url.openConnection();

                // Check if connection is successful
                int code = urlConnection.getResponseCode();
                if (code !=  200) {
                    throw new IOException("Invalid response from server: " + code);
                }

                BufferedReader buffReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder strBuilder = new StringBuilder();

                String line = null;
                while ((line = buffReader.readLine()) != null) {
                    Log.i("JSON String: ", line);
                    strBuilder.append(line + "\n");
                }
                result = strBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Disconnect all connections
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            System.out.println("End of background execution.");
            return null;
        }

        protected void onPostExecute(Void v) {
            // Parse JSON data to display in ListView
            try {
                System.out.println("Start of post execution.");
                JSONArray jsonArray = new JSONArray(result);
                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    String name = jsonObj.getString("name");
                    String description = jsonObj.getString("description");
                    String price = jsonObj.getString("price");

                    catalogList.add(name);
                    catalogList.add(description);
                    catalogList.add(price);
                }
                System.out.println("End of post execution.");
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            }
        }
    }
}