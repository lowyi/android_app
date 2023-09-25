package com.example.clicknship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RootBeer rootBeer = new RootBeer(this);
        if (rootBeer.isRooted()) {
            //we found indication of root
            new AlertDialog.Builder(this)
                    .setTitle("Phone Is Rooted")
                    .setMessage("Your device has been rooted. To protect your data safety, you are not allowed to use this app.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finishAffinity();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        AccountManager am = AccountManager.get(this);
        RequestQueue queue = Volley.newRequestQueue(this);

        TextView username =  findViewById(R.id.username);
        TextView Password =  findViewById(R.id.password);
        ImageView ShopBtn =  findViewById(R.id.ShopBtn);
        Button loginBtn =  findViewById(R.id.loginbtn);

        //ShopBtn Login
        ShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();

                tokenRequest tokenRequest = new tokenRequest(Request.Method.POST, "https://authorization-server.com/authorize", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse.statusCode == 401) {
                            //refreshAccessToken();
                        } else {
                            // irrecoverable errors. show error to user.
                            Toast.makeText(MainActivity.this, "onErrorResponse:token request "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                queue.add(tokenRequest);
            }
        });

        //login button
        loginBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //To authenticate with username and password
                String url = "https://jsonplaceholder.typicode.com/todos/1";
                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("username", username.getText().toString());
                    //sha256 hash for password
                    jsonParams.put("password", getSha256Hash(Password.getText().toString()));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    //int userID = response.getInt("userId");
                                    String Result = response.getString("title");

                                    if (Result.equals("OK")) {
                                        Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                        //To switch to second view(page) catalog
                                        Intent intent = new Intent(getApplicationContext(),Catalog.class);
                                        startActivity(intent);

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "error:" + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "login unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(jsonObjectRequest);

            }
        });

    }

    public static String getSha256Hash(String password) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            digest.reset();
            return bin2hex(digest.digest(password.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }
}