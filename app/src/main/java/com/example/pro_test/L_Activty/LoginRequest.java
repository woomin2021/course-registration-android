package com.example.pro_test.L_Activty;



import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8080/UserLogin.jsp";
    private final Map<String, String> params;

    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        params = new HashMap<>();
        params.put("userID", userID);
        params.put("userPassword", userPassword);
    }

    @Override
    protected Map<String, String> getParams() {
        return params;
    }
}
