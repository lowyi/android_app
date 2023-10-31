package com.example.clicknship;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

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

    @Override
    public void onBackPressed() {
        if (itemDescription.canGoBack()) {
            itemDescription.goBack();
        } else {
            super.onBackPressed();
        }
    }
}