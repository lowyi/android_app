package com.example.clicknship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    public OkHttpClient client;
    public static final MediaType CONTENT_TYPE = MediaType.get("application/x-www-form-urlencoded");
    String apUrl = "www.example.com/oauth/token"; // replace host url through your oauth2 server.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.username);
        TextView Password = (TextView) findViewById(R.id.password);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        ImageView ShopBtn = (ImageView) findViewById(R.id.ShopBtn);

        //ShopBtn Login
        ShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(new BasicAuthInterceptor("USER_CLIENT_APP", "password"))
                        .build();

                String username = "user";
                String password = "password";
                String grant_type = "password";
                String requestData = "grant_type=" + grant_type + "&username=" + username + "&password=" + password;

                try {
                    loginRequest(apUrl, requestData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //login button
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (username.getText().toString().equals("HAHA")){
                   Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT);
               }
            }
        });

    }

    public void loginRequest(String apUrl, String requestData) throws IOException {

        RequestBody body = RequestBody.create(CONTENT_TYPE, requestData);
        Request request = new Request.Builder()
                .url(apUrl)
                .addHeader("Content-Type", " application/x-www-form-urlencoded")
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Log.e("CallBackResponse", "onFailure() Request was: " + call.request());
                Toast.makeText(MainActivity.this, "onFailure() Request was: " + call.request(), Toast.LENGTH_SHORT);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.i("CallBackResponse ", "onResponse(): " + response.body().string());
                Toast.makeText(MainActivity.this, "onResponse(): " + response.body().string(), Toast.LENGTH_SHORT);
                //success response here

            }
        });
    };
}