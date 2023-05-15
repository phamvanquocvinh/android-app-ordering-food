package com.example.finalproject.UserActivity;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.LoginActivity;
import com.example.finalproject.Model.User;
import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {
    public static final int REQUEST_CODE_ORDER_HISTORY = 1;
    public static final int REQUEST_CODE_UPDATE_PROFILE = 2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    ImageView imgAvatar;
    TextView tvUserFullName;
    ImageButton btnEditProfile;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvAddress;
    Button btnOrderHistory;
    Button btnLogout;
    private ProgressDialog progressDialog;
    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        // Ánh xạ các View trong fragment_profile.xml
        initUi();
        showUserInformation();
        initListener();
        // Trả về View của Fragment
        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ORDER_HISTORY && resultCode == RESULT_OK) {
            // Thực hiện các hành động để quay trở lại trang FragmentProfile trước đó
        }
        if(requestCode == REQUEST_CODE_UPDATE_PROFILE && resultCode == RESULT_OK){
            showUserInformation();
        }
    }

    private void initUi(){
        imgAvatar = mView.findViewById(R.id.img_avatar);
        tvUserFullName= mView.findViewById(R.id.tvUserFullName);
        tvPhone = mView.findViewById(R.id.tvPhone);
        tvEmail = mView.findViewById(R.id.tvEmail);
        tvAddress = mView.findViewById(R.id.tvAddress);
        btnLogout = mView.findViewById(R.id.btnLogout);
        btnOrderHistory = mView.findViewById(R.id.btnOderHistory);
        btnEditProfile = mView.findViewById(R.id.btnEditProfile);
        progressDialog = new ProgressDialog(getActivity());
    }

    private void initListener(){
        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderHistoryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ORDER_HISTORY);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình chỉnh sửa thông tin cá nhân
                onclickEditProfile();
            }
        });

        // Xử lý sự kiện khi nhấn nút đăng xuất
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onClickLogout();
            }
        });
    }

    private void onclickEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PROFILE);
    }

    private void onClickLogout() {
        // Tạo dialog xác nhận
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Thực hiện đăng xuất và chuyển sang trang đăng nhập
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Không thực hiện đăng xuất
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showUserInformation(){
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myUserRef = database.getReference("list_users/"+user.getUid());

        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                String name = user.getUserFullName();
                String phone = user.getPhoneNumber();
                String email = user.getEmail();
                String address = user.getAddress();
                //String photoUri = "";

                if(name == null){
                    tvUserFullName.setVisibility(View.GONE);
                } else {
                    tvUserFullName.setVisibility(View.VISIBLE);
                    tvUserFullName.setText(name);
                }

                if(phone == null){
                    tvPhone.setVisibility(View.GONE);
                } else {
                    tvPhone.setVisibility(View.VISIBLE);
                    tvPhone.setText(phone);
                }

                if(email == null){
                    tvEmail.setVisibility(View.GONE);
                } else {
                    tvEmail.setVisibility(View.VISIBLE);
                    tvEmail.setText(email);
                }

                if(address == null){
                    tvAddress.setVisibility(View.GONE);
                } else {
                    tvAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(address);
                }
                progressDialog.dismiss();
                //Glide.with(getActivity()).load(photoUri).error(R.drawable.default_user_avatar).into(imgAvatar);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
