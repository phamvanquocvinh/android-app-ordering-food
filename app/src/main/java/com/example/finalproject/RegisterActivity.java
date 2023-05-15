package com.example.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Model.User;
import com.example.finalproject.UserActivity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText edtFullName;
    EditText edtPhone;
    EditText edtEmail;
    EditText edtAddress;
    EditText edtPassword;
    EditText edtConfirmPassword;
    Button btnRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        initUI();
        initListener();
    }
    private void initUI(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
    }

    private void initListener(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickSignUp();
            }
        });
    }
    private void onclickSignUp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();
        String fullname = edtFullName.getText().toString().trim();
        String phoneNumber = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // email is invalid
            Toast.makeText(RegisterActivity.this, "Vui lòng kiểm tra lại email",
                    Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            // check null password
            Toast.makeText(RegisterActivity.this, "Mật khẩu không được để trống",
                    Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6) {
            // check length password
            Toast.makeText(RegisterActivity.this, "Mật khẩu phải tối thiểu 6 kí tự",
                    Toast.LENGTH_SHORT).show();
        }
        else if (!confirmPassword.equals(password)) {
            // check length password
            Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp",
                    Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(fullname)) {
            // check null password
            Toast.makeText(RegisterActivity.this, "Họ tên không được để trống",
                    Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phoneNumber)) {
            // check null password
            Toast.makeText(RegisterActivity.this, "Số điện thoại không được để trống",
                    Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address)) {
            // check null password
            Toast.makeText(RegisterActivity.this, "Địa chỉ không được để trống",
                    Toast.LENGTH_SHORT).show();
        } else {
            // user information is valid
            progressDialog.show();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                User user = new User(mAuth.getUid(), email , fullname, phoneNumber, address);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("list_users");
                                String pathObject = String.valueOf(user.getId());
                                myRef.child(pathObject).setValue(user);
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Email đã tồn tại",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}