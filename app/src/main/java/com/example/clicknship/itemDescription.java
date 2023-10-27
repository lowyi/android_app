package com.example.clicknship;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class itemDescription extends AppCompatActivity {

    private WebView itemDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        Intent intent = getIntent();
        String itemName = intent.getStringExtra(EXTRA_MESSAGE);
        Toast.makeText(itemDescription.this, "new view with test" + itemName, Toast.LENGTH_SHORT).show();
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