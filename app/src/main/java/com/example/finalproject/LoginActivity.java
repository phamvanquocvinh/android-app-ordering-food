package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.AdminActivity.MainActivityAdmin;
import com.example.finalproject.UserActivity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    Button btnRegister;
    private LinearLayout layoutForgotPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //init UI
        initUi();
        initListener();

    }

    private void initUi() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        layoutForgotPassword = findViewById(R.id.layoutForgotpass);
        progressDialog = new ProgressDialog(this);

    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickRegister();
            }
        });
        layoutForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickForgotPassword();
            }
        });
    }

    private void onclickForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void onclickRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }


    private void onclickLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if(email.equals("admin") && password.equals("123456")){
            Intent intent = new Intent(LoginActivity.this, MainActivityAdmin.class);
            startActivity(intent);
            finishAffinity();
        }else {
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // email is invalid
                Toast.makeText(LoginActivity.this, "Vui lòng kiểm tra lại email",
                        Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(password)){
                //check null password
                Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẩu",
                        Toast.LENGTH_SHORT).show();
            }
            else if(password.length() < 6) {
                //check length password
                Toast.makeText(LoginActivity.this, "Mật khẩu phải ít nhất 6 kí tự",
                        Toast.LENGTH_SHORT).show();
            } else {
                // email, password is valid
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user != null){

                                    }
                                    user.getUid();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không chính xác",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
}