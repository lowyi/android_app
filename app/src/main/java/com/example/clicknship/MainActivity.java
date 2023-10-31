package com.example.clicknship;

import static com.example.clicknship.PasswordUtil.encryptStrAndToBase64;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "client-app";
    private static final String CLIENT_SECRET = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

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
        Button loginBtn =  findViewById(R.id.loginbtn);

        //login button
        loginBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //To authenticate with username and password
                //String url = "http://34.30.227.181:4200/api/authenticationService/oauth/token";
                String url = "http://192.168.1.71:8770/api/authenticationService/oauth/token";

                if (!url.startsWith("file://")) {
                    if (!url.startsWith("javascript://")) {
                        if (!url.startsWith("content://")) {

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

                                                Intent intent = new Intent(MainActivity.this,Catalog.class);
                                                intent.putExtra("expire", obj.getString("expiresIn"));
                                                startActivity(intent);

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

                        }else{
                            new AlertDialog.Builder(MainActivity.this)
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
                        new AlertDialog.Builder(MainActivity.this)
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
                    new AlertDialog.Builder(MainActivity.this)
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

    }

}