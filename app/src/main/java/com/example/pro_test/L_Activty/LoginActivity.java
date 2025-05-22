package com.example.pro_test.L_Activty;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.pro_test.MainActivity;
import com.example.pro_test.R;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etUserID, etPassword;
    private Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUserID = findViewById(R.id.etUserID);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        btnLogin.setOnClickListener(v -> {
            Log.d("LoginTest", "로그인 버튼 눌림");
            String userID = etUserID.getText().toString();
            String password = etPassword.getText().toString();

            if (userID.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "모든 정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("LoginTest", "서버 응답: " + response);  // ★ 응답 확인
                    JSONObject json = new JSONObject(response);
                    if (json.getBoolean("success")) {
                        Log.d("LoginTest2, msg 서버 성공 : " , String.valueOf(json.getBoolean("success")));
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("userID", userID);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            LoginRequest loginRequest = new LoginRequest(userID, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}