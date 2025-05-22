package com.example.pro_test.L_Activty;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.pro_test.R;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private String userID;
    private String userPassword;
    private String username;
    private String userEmail;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText etId = findViewById(R.id.etId);
        final EditText etPasswd = findViewById(R.id.etPasswd);
        final EditText emailTxt = findViewById(R.id.etEmail);
        final EditText etName = findViewById(R.id.etName);
        final Button btnValidate = findViewById(R.id.btnValidate);
        final Button btnMembership = findViewById(R.id.btnMembership);

        // 아이디 중복 확인
        btnValidate.setOnClickListener(view -> {
            String userID = etId.getText().toString().trim();

            if (userID.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                etId.requestFocus();
                return;
            }

            if (validate) return;

            Response.Listener<String> responseListener = response -> {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean newID = jsonResponse.getBoolean("newID");
                    Log.d("mytest", jsonResponse.toString());

                    if (newID) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        etId.setEnabled(false);
                        etId.setBackgroundColor(Color.GRAY);
                        btnValidate.setBackgroundColor(Color.GRAY);
                        validate = true;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                .setNegativeButton("확인", null)
                                .create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(validateRequest);
        });

        // 회원가입 요청
        btnMembership.setOnClickListener(view -> {
            userID = etId.getText().toString();
            userPassword = etPasswd.getText().toString();
            userEmail = emailTxt.getText().toString();
            username = etName.getText().toString();

            Log.d("RegisterInput", "userID=" + userID + ", userPassword=" + userPassword
                    + ", username=" + username + ", userEmail=" + userEmail);

            if (userID.isEmpty() || userPassword.isEmpty() || userEmail.isEmpty() || username.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                dialog = builder.setMessage("모든 정보를 입력해주세요.")
                        .setNegativeButton("확인", null)
                        .create();
                dialog.show();
                return;
            }

            Response.Listener<String> responseListener = response -> {
                try {
                    Log.d("RegisterInput", "서버 응답: " + response);
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        dialog = builder.setMessage("회원으로 등록되었습니다.")
                                .setPositiveButton("확인", (dialogInterface, i) -> finish()) // 여기서 종료
                                .create();
                        dialog.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            RegisterRequest registerRequest = new RegisterRequest(
                    userID, userPassword, username, userEmail, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}