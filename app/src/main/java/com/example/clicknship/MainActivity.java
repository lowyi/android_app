package com.example.clicknship;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
            }
        });

        //login button
        loginBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //To authenticate with username and password
                //String url = "http://34.30.227.181:4200/api/authenticationService/oauth/token";
                String url = "http://192.168.1.71:8770/api/authenticationService/oauth/token";

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Toast.makeText(MainActivity.this, "login successful" + response, Toast.LENGTH_SHORT).show();
                                //To switch to second view(page) catalog
                                Intent intent = new Intent(MainActivity.this,Catalog.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "login unsuccessful" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
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
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("grant_type", "password");
                        params.put("client_id", CLIENT_ID);
                        params.put("client_secret", CLIENT_SECRET);
                        params.put("username", username.getText().toString());
                        params.put("password", BCrypt.withDefaults().hashToString(10, Password.getText().toString().toCharArray()));
                        params.put("otp", BCrypt.withDefaults().hashToString(10, otp.getText().toString().toCharArray()));

                        return params;
                    }
                };

                queue.add(postRequest);
            }
        });

    }

    private void refreshAccessToken(String  refreshToken, String username) {
        Preferences pre = new Preferences();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        //refresh token server
        String url = "http://34.30.227.181:4200/api/authenticationService/oauth/token";

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("refresh_token", refreshToken);
            jsonParams.put("username", username);
            jsonParams.put("client_id", CLIENT_ID);
            jsonParams.put("client_secret", CLIENT_SECRET);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //int userID = response.getInt("userId");
                            String token = response.getString("access_token");
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