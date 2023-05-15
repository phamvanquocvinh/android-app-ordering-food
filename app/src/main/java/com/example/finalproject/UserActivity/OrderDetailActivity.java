package com.example.finalproject.UserActivity;

import static com.example.finalproject.UserActivity.MainActivity.database;
import static com.example.finalproject.UserActivity.MainActivity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.FoodListInOrderDetailAdapter;
import com.example.finalproject.Model.Food;
import com.example.finalproject.R;
import com.google.firebase.database.DatabaseReference;

import java.text.NumberFormat;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    TextView tvOrderID;
    TextView tvOderUserFullName;
    TextView tvOderAddress;
    TextView tvOrderPhone;
    TextView tvOrderTotalAmount;
    TextView tvOrderDate;
    Button btnStatus;
    Button btnExit;
    ListView lvOrderHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);
        tvOrderID = findViewById(R.id.tvOrderID);
        tvOderUserFullName = findViewById(R.id.tvOderUserFullName);
        tvOderAddress = findViewById(R.id.tvOderAddress);
        tvOrderPhone = findViewById(R.id.tvOrderPhone);
        tvOrderTotalAmount = findViewById(R.id.tvOrderTotalAmount);
        tvOrderDate = findViewById(R.id.tvOrderDate);

        btnStatus = findViewById(R.id.btnStatus);
        btnExit = findViewById(R.id.btnExit);
        lvOrderHistory = findViewById(R.id.lvOrderHistory);

        // Lấy dữ liệu từ Intent (nếu có) và truyền vao giao diện
        Intent intent = getIntent();
        int orderCode = intent.getIntExtra("orderCode", 0);
        tvOrderID.setText(String.valueOf(orderCode));
        int totalAmount = intent.getIntExtra("totalAmount", 0);
        NumberFormat formatter = NumberFormat.getInstance();
        String formattedPrice = formatter.format(totalAmount);
        tvOrderTotalAmount.setText(String.valueOf(formattedPrice) + " VND");
        String orderDate = intent.getStringExtra("orderDate");
        tvOrderDate.setText(orderDate);
        String userFullName = intent.getStringExtra("userFullName");
        tvOderUserFullName.setText(userFullName);
        String phoneNumber = intent.getStringExtra("phoneNumber");
        tvOrderPhone.setText(phoneNumber);
        String address = intent.getStringExtra("address");
        tvOderAddress.setText(address);

        List<Food> foodList = (List<Food>) intent.getSerializableExtra("foodList");

        // Tạo adapter cho lvOrderHistory
        FoodListInOrderDetailAdapter adapter = new FoodListInOrderDetailAdapter(this, foodList);
        lvOrderHistory.setAdapter(adapter);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference orderUserRef = database.getReference("list_users/"+ user.getUid()+"/orders");
                DatabaseReference ordersRef = database.getReference("orders");
                orderUserRef.child(String.valueOf(orderCode)).child("status").setValue("Đã nhận hàng");
                ordersRef.child(String.valueOf(orderCode)).child("status").setValue("Đã nhận hàng");
                setResult(RESULT_OK);
                finish();
            }
        });

    }

}
