package com.example.clicknship;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class itemDescription extends AppCompatActivity {

    private WebView itemDescription;

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

        Toast.makeText(itemDescription.this, "result" + position, Toast.LENGTH_SHORT).show();

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