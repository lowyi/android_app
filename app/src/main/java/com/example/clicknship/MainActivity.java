package com.example.clicknship;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.username);
        TextView Password = (TextView) findViewById(R.id.password);
        ImageView ShopBtn = (ImageView) findViewById(R.id.ShopBtn);
        Button loginBtn = (Button) findViewById(R.id.loginbtn);

        //ShopBtn Login
        ShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();

            }
        });

        //login button
        loginBtn.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("HAHA")) {
                    Toast.makeText(MainActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}