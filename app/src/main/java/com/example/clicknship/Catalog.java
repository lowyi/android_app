package com.example.clicknship;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Catalog extends AppCompatActivity {

    private WebView Catalog;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_catalog);

        Button searchBtn = findViewById(R.id.searchB);
        TextView item =  findViewById(R.id.search);
        ListView productList = findViewById(R.id.productListView);
        ArrayList<String> itemDescription = new ArrayList<>();

        final String searchURL = "http://192.168.1.71:8770/api/catalogueService/catalogue/catalogue/search?page=0&size=100";
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonBody = new JSONObject();

        Intent intent = getIntent();
        String expired = intent.getStringExtra("expire");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                                            try {
                                                JSONArray objArray = new JSONArray(jsonStr);
                                                ArrayList<String> itemList = new ArrayList<>();
                                                itemDescription.clear();
                                                for (int i = 0; i < objArray.length(); i++) {
                                                    JSONObject row = objArray.getJSONObject(i);
                                                    itemList.add(row.getString("name"));
                                                    itemDescription.add(row.getString("description"));
                                                }

                                                ArrayAdapter arrayAdapter = new ArrayAdapter(Catalog.this, android.R.layout.simple_list_item_1, itemList);
                                                productList.setAdapter(arrayAdapter);

                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            Toast.makeText(Catalog.this, "Result return successful", Toast.LENGTH_SHORT).show();

                                        }
                                    },
                                    new Response.ErrorListener()
                                    {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(Catalog.this, "Result return unsuccessful" + error, Toast.LENGTH_SHORT).show();
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
            }
        });

        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(Catalog.this, itemDescription.class);
                //String message = parent.getAdapter().getItem(position).toString();
                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("description", itemDescription.get(position).toString());

                startActivity(intent);
            }
        });

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
                                    try {
                                        JSONArray objArray = new JSONArray(jsonStr);
                                        ArrayList<String> itemList = new ArrayList<>();
                                        itemDescription.clear();
                                        for (int i = 0; i < objArray.length(); i++) {
                                            JSONObject row = objArray.getJSONObject(i);
                                            itemList.add(row.getString("name"));
                                            itemDescription.add(row.getString("description"));
                                        }

                                        ArrayAdapter arrayAdapter = new ArrayAdapter(Catalog.this, android.R.layout.simple_list_item_1, itemList);
                                        productList.setAdapter(arrayAdapter);

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Toast.makeText(Catalog.this, "Catalog retrieve successful", Toast.LENGTH_SHORT).show();

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
                                jsonBody.put("searchTerm", "");
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

        startTimer(expired);

    }

    public void startTimer( String expired) {

        //cTimer = new CountDownTimer(30000, 1000) {

        int futureTime = Integer.parseInt(expired) * 10;

        cTimer = new CountDownTimer(futureTime, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Toast.makeText(Catalog.this, "Session have expired, Kindly re-login again.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Catalog.this,MainActivity.class);
                startActivity(intent);
            }
        };
        cTimer.start();
    };

    @Override
    public void onBackPressed() {
        if (Catalog.canGoBack()) {
            Catalog.goBack();
        } else {
            super.onBackPressed();
        }
    }

}