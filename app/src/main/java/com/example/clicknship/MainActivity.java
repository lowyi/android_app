package com.example.clicknship;

import static com.example.clicknship.PasswordUtil.encryptStrAndToBase64;

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
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
                                String jsonStr = response;
                                try {
                                    Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                    JSONObject obj = new JSONObject(jsonStr);
                                    pre.setAccessToken(MainActivity.this,obj.getString("token"), obj.getString("refreshToken"));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                Intent intent = new Intent(MainActivity.this,Catalog.class);
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener()
                        {
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
                        params.put("username", username.getText().toString().trim());
                        try {
                            byte[] dataP = Password.getText().toString().trim().getBytes("UTF-8");
                            byte[] dataPK = "go2shopSecretKey".getBytes("UTF-8");

                            params.put("password", encryptStrAndToBase64(dataPK, dataP));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            byte[] dataOTP = otp.getText().toString().trim().getBytes("UTF-8");
                            byte[] dataOTPK = "go2shopSecretKey".getBytes("UTF-8");

                            params.put("otp", encryptStrAndToBase64(dataOTPK, dataOTP));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        return params;
                    }
                };

                queue.add(postRequest);
            }
        });

    }

    private void refreshAccessToken(String  Token, String username) {
        String url = "http://192.168.1.71:8770/api/authenticationService/oauth/token";
        Preferences pre = new Preferences();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        String jsonStr = response;
                        try {
                            Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                            JSONObject obj = new JSONObject(jsonStr);
                            pre.setAccessToken(MainActivity.this,obj.getString("token"), obj.getString("refreshToken"));
                            Toast.makeText(MainActivity.this, "token:" + obj.getString("token"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
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
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", "refresh_token");
                params.put("client_id", CLIENT_ID);
                params.put("client_secret", CLIENT_SECRET);
                params.put("username", username.trim());
                params.put("token", Token);
//                try {
//                    //params.put("password",  encryptStrAndToBase64("Z28yc2hvcFNlY3JldEtleQ==", Password.getText().toString()));
//                    params.put("password","BoKxn6eLtxE53lh/8Sb4CA==");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//                try {
//                    params.put("otp", encryptStrAndToBase64("Z28yc2hvcFNlY3JldEtleQ==", otp.getText().toString()));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }

                return params;
            }
        };

        queue.add(postRequest);
    }
}