package com.example.finalproject.AdminActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.finalproject.LoginActivity;
import com.example.finalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityAdmin extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_admin);

        // Khởi tạo Fragment Manager
        fragmentManager = getSupportFragmentManager();

        // Mặc định hiển thị fragment home khi khởi chạy MainActivityAdmin
        loadFragment(new HomeAdminFragment());

        // Xử lý sự kiện khi chọn các tab trong BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_admin);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeAdminFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.order_list:
                    fragment = new OrderListAdminFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.logout:
                    new AlertDialog.Builder(MainActivityAdmin.this)
                            .setTitle("Đăng xuất")
                            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                            .setPositiveButton("có", (dialog, which) -> {
                                // Đăng xuất và quay trở về trang đăng nhập
                                Intent intent = new Intent(MainActivityAdmin.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton("không", null)
                            .show();
                    return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}

