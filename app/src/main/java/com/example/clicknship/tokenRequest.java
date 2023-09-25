package com.example.clicknship;

import android.util.Base64;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class tokenRequest extends StringRequest {
    private static final String CLIENT_ID = "client-app";
    private static final String CLIENT_SECRET = "123456";

    public tokenRequest(int method, String url, Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        String auth = "Basic "
                + Base64.encodeToString((CLIENT_ID
                        + ":" + CLIENT_SECRET).getBytes(),
                Base64.NO_WRAP);
        headers.put("Authorization", auth);
        return headers;
    }
}
