package com.example.clicknship;

import android.util.Base64;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class tokenRequest extends StringRequest {
    private static final String CLIENT_ID = "Tj5vubajGQlfS7FhXeboS9e1";
    private static final String CLIENT_SECRET = "F8_c2yhhQudh4eGv1IrnAeZliDqVG9b4dWHrNvTVm5EE54Zc";

    public tokenRequest(int method, String url, Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "client_credentials");
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
