package com.example.clicknship;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
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

        AccountManager am = AccountManager.get(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        Bundle options = new Bundle();

        TextView username =  findViewById(R.id.username);
        TextView Password =  findViewById(R.id.password);
        ImageView ShopBtn =  findViewById(R.id.ShopBtn);
        Button loginBtn =  findViewById(R.id.loginbtn);

        //ShopBtn Login
        ShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();

                Account[] accounts = am.getAccountsByType("com.google");

//                am.getAuthToken(
//                        account,                     // Account retrieved using getAccountsByType()
//                        "Manage your tasks",            // Auth scope
//                        options,                        // Authenticator-specific options
//                        this,                           // Your activity
//                        new OnTokenAcquired(),          // Callback called when a token is successfully acquired
//                        new Handler(new OnError()));    // Callback called if an error occurs

            }
        });

        //login button
        loginBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                //Need to append password hash to the end of URL then get the respond
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

                                        final Account account = new Account(username.getText().toString(), "login");
                                        am.addAccountExplicitly(account, Password.getText().toString(), null);
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

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            // Get the result of the operation from the AccountManagerFuture.
            Bundle bundle = null;
            try {
                bundle = result.getResult();

            } catch (AuthenticatorException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (OperationCanceledException e) {
                throw new RuntimeException(e);
            }

            // The token is a named value in the bundle. The name of the value
            // is stored in the constant AccountManager.KEY_AUTHTOKEN.
            String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

        }
    }
}