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
import com.clicknship.dto.Iuserlogin;
import com.scottyab.rootbeer.RootBeer;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

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
        TextView otp =  findViewById(R.id.otp);
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
                //String url = "https://jsonplaceholder.typicode.com/todos/1";
                String url = "http://34.30.227.181:4200/api/authenticationService/securityUser/login";
                Iuserlogin login =  new Iuserlogin();
                login.setUsername(username.getText().toString());
                try {
                    login.setPassword(AESencryption(Password.getText().toString()));
                    login.setOtp(AESencryption(otp.getText().toString()));
                } catch (InvalidKeyException e) {
                    throw new RuntimeException(e);
                } catch (IllegalBlockSizeException e) {
                    throw new RuntimeException(e);
                } catch (BadPaddingException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchPaddingException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

                JSONObject jsonParams = new JSONObject();
                try {
                    jsonParams.put("userlogin", login);
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
                                Toast.makeText(MainActivity.this, "login unsuccessful" + error, Toast.LENGTH_SHORT).show();
                            }
                        });
                queue.add(jsonObjectRequest);

            }
        });

    }

    public static String AESencryption( String txt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        byte[] plaintext = txt.getBytes();
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        SecretKey key = keygen.generateKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = cipher.doFinal(plaintext);
        byte[] iv = cipher.getIV();
        return  bin2hex(ciphertext);
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }
}