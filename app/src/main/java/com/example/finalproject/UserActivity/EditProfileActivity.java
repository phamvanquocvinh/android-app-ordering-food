package com.example.finalproject.UserActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtFullName;
    EditText edtPhone;
    EditText edtEmail;
    EditText edtAddress;
    ImageView imgAvatar;
    Button btnExit;
    Button btnUpdate;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        initUi();
        setUserInformation();
        initListener();
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myUserRef = database.getReference("list_users/"+user.getUid());

        myUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String idUser = snapshot.getKey();
                String name = snapshot.child("userFullName").getValue(String.class);
                String phone = snapshot.child("phoneNumber").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                String photoUri = String.valueOf(user.getPhotoUrl());

                if(name == null){
                    edtFullName.setVisibility(View.GONE);
                } else {
                    edtFullName.setVisibility(View.VISIBLE);
                    edtFullName.setText(name);
                }

                if(phone == null){
                    edtPhone.setVisibility(View.GONE);
                } else {
                    edtPhone.setVisibility(View.VISIBLE);
                    edtPhone.setText(phone);
                }

                if(email == null){
                    edtEmail.setVisibility(View.GONE);
                } else {
                    edtEmail.setVisibility(View.VISIBLE);
                    edtEmail.setText(email);
                }

                if(address == null){
                    edtAddress.setVisibility(View.GONE);
                } else {
                    edtAddress.setVisibility(View.VISIBLE);
                    edtAddress.setText(address);
                }
                Glide.with(EditProfileActivity.this).load(photoUri).error(R.drawable.default_user_avatar).into(imgAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initListener() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentResult = new Intent();

                setResult(RESULT_OK);

                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onclickUpdate();
                Intent intentResult = new Intent();

                setResult(RESULT_OK);

                finish();
            }
        });
    }

    private void onclickUpdate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) return;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myUserRef = database.getReference("list_users/"+user.getUid());
        myUserRef.child("userFullName").setValue(edtFullName.getText().toString());
        myUserRef.child("phoneNumber").setValue(edtPhone.getText().toString());
        myUserRef.child("address").setValue(edtAddress.getText().toString());
    }

    private void initUi() {
        imgAvatar = findViewById(R.id.img_avatar);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        btnExit = findViewById(R.id.btnExit);
        btnUpdate = findViewById(R.id.btnUpdate);
        progressDialog = new ProgressDialog(this);
    }


}
