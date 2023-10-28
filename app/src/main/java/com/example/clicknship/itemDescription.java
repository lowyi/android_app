package com.example.clicknship;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class itemDescription extends AppCompatActivity {

    private WebView itemDescription;
    CountDownTimer cTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        ImageView itemImage =  findViewById(R.id.itemImage);
        TextView itemDescription =  findViewById(R.id.itemDescription);
        int product[] = {R.drawable.crystal_tumbles, R.drawable.samsung};

        Intent intent = getIntent();
        String itemposition = intent.getStringExtra("position");
        String description = intent.getStringExtra("description");
        int position = Integer.parseInt(itemposition.toString());

        itemDescription.setText(description.toString());
        itemImage.setImageResource(product[position]);
    }

    public void startTimer( String expired) {

        //cTimer = new CountDownTimer(30000, 1000) {

        int futureTime = Integer.parseInt(expired) * 10;

        cTimer = new CountDownTimer(futureTime, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                Toast.makeText(itemDescription.this, "Session have expired, Kindly re-login again.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(itemDescription.this,MainActivity.class);
                startActivity(intent);
            }
        };
        cTimer.start();
    };

    @Override
    public void onBackPressed() {
        if (itemDescription.canGoBack()) {
            itemDescription.goBack();
        } else {
            super.onBackPressed();
        }
    }
}