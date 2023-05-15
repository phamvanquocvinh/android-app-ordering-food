package com.example.finalproject.UserActivity;

import static com.example.finalproject.UserActivity.MainActivity.database;
import static com.example.finalproject.UserActivity.MainActivity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Model.OrderHistory;
import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    private ListView listView;
    private OrderHistoryAdapter adapter;
    private ArrayList<OrderHistory> orderHistoryList = new ArrayList<>();

    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);
        listView = findViewById(R.id.lvOrderHistory);
        // Tạo data
        getListOrderHistory();

        // Tạo adapter và thiết lập cho ListView
        adapter = new OrderHistoryAdapter(this, orderHistoryList);

        listView.setAdapter(adapter);

        btnExit = findViewById(R.id.btnExit);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListOrderHistory();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void getListOrderHistory() {
        DatabaseReference orderRef = database.getReference("list_users/" + user.getUid() + "/orders");
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderHistoryList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    OrderHistory order = dataSnapshot.getValue(OrderHistory.class);
                    orderHistoryList.add(order);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
