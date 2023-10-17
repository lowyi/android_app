package com.example.clicknship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.clicknship.dto.tokenlogin;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "client-app";
    private static final String CLIENT_SECRET = "123456";

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

        Preferences pre = new Preferences();
        RequestQueue queue = Volley.newRequestQueue(this);

        TextView username =  findViewById(R.id.username);
        TextView Password =  findViewById(R.id.password);
        TextView otp =  findViewById(R.id.otp);
        ImageView ShopBtn =  findViewById(R.id.ShopBtn);
        Button loginBtn =  findViewById(R.id.loginbtn);

        //ShopBtn Login
        ShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://authorization-server.com/authorize";

                StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                //To switch to second view(page) catalog
                                Intent intent = new Intent(MainActivity.this,Catalog.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Toast.makeText(MainActivity.this, "login unsuccessful" + error, Toast.LENGTH_SHORT).show();
//                                if (error.networkResponse.statusCode == 'A103') {
//                                    refreshAccessToken(pre.getRefreshToken(MainActivity.this),username.getText().toString());
//                                } else {
//                                    // irrecoverable errors. show error to user.
//                                    Toast.makeText(MainActivity.this, "onErrorResponse:token request "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  headers = new HashMap<String, String>();
//                        String auth = "Basic "
//                                + Base64.encodeToString((CLIENT_ID
//                                        + ":" + CLIENT_SECRET).getBytes(),
//                                Base64.NO_WRAP);
                        String Bearer = "Bearer " +  pre.getAccessToken(MainActivity.this);
                        headers.put("Authorization", Bearer);

                        return headers;
                    }
                };

                queue.add(getRequest);

//                tokenRequest tokenRequest = new tokenRequest(Request.Method.POST, "https://authorization-server.com/authorize", new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (error.networkResponse.statusCode == 401) {
//                            //refreshAccessToken();
//                        } else {
//                            // irrecoverable errors. show error to user.
//                            Toast.makeText(MainActivity.this, "onErrorResponse:token request "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//                queue.add(tokenRequest);
            }
        });

        //login button
        loginBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //To authenticate with username and password
                String url = "http://34.30.227.181:4200/api/authenticationService/oauth/token";
                tokenlogin tokenlog =  new tokenlogin();
                tokenlog.setUsername(username.getText().toString());
                tokenlog.setPassword(BCrypt.withDefaults().hashToString(10, Password.getText().toString().toCharArray()));
                tokenlog.setOtp(BCrypt.withDefaults().hashToString(10, otp.getText().toString().toCharArray()));

                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("userlogin", tokenlog);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //Intent intent = new Intent(MainActivity.this,Catalog.class);
                //startActivity(intent);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    //int userID = response.getInt("userId");
                                    String token = response.getString("token");
                                    String refreshToken = response.getString("refresh_token");

                                    if (response.equals("OK")) {
                                        Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                        //STORED TOKEN INTO PREFERENCES
                                        pre.setAccessToken(MainActivity.this,token,refreshToken);
                                        //To switch to second view(page) catalog
                                        Intent intent = new Intent(MainActivity.this,Catalog.class);
                                        startActivity(intent);

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "error:" + e, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "login unsuccessful" + error, Toast.LENGTH_SHORT).show();
//                                if (error.networkResponse.statusCode == 'A103') {
//                                    refreshAccessToken(pre.getRefreshToken(MainActivity.this),username.getText().toString());
//                                } else {
//                                    // irrecoverable errors. show error to user.
//                                    Toast.makeText(MainActivity.this, "onErrorResponse:token request "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String>  headers = new HashMap<String, String>();
                                String auth = "Basic "
                                        + Base64.encodeToString((CLIENT_ID
                                                + ":" + CLIENT_SECRET).getBytes(),
                                        Base64.NO_WRAP);
                                //String Bearer = "Bearer " +  pre.getAccessToken(MainActivity.this);
                                headers.put("Authorization", auth);

                                return headers;
                            }
                        };

                queue.add(jsonObjectRequest);

            }
        });

    }

    private void refreshAccessToken(String  refreshToken, String username) {
        Preferences pre = new Preferences();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        //refresh token server
        String url = "http://34.30.227.181:4200/ ";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("refresh_token", refreshToken);
            jsonParams.put("username", username);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //int userID = response.getInt("userId");
                            String token = response.getString("token");
                            String refreshToken = response.getString("refresh_token");

                            Toast.makeText(MainActivity.this, "refresh_token successful", Toast.LENGTH_SHORT).show();
                            //STORED TOKEN INTO PREFERENCES
                            pre.setAccessToken(MainActivity.this,token,refreshToken);

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "error:" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MainActivity.this, "onErrorResponse:token request "+ error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        queue.add(jsonObjectRequest);
    }
}