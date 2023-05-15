package com.example.finalproject.UserActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalproject.Model.Food;
import com.example.finalproject.Model.OrderHistory;
import com.example.finalproject.R;

import java.util.ArrayList;

public class OrderHistoryAdapter extends ArrayAdapter<OrderHistory> {
    private Context context;
    private ArrayList<OrderHistory> orderHistoryList;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderHistoryList) {
        super(context, 0, orderHistoryList);
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderHistory orderHistory = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_history_list, parent, false);
        }

        // Ánh xạ các view trong layout
        TextView tvOrderID = convertView.findViewById(R.id.tvOrderID);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        ImageButton btnOrderInfo = convertView.findViewById(R.id.btnOrderInfo);

        // Hiển thị dữ liệu
        tvOrderID.setText(String.valueOf(orderHistory.getOrderCode()));
        tvStatus.setText(String.valueOf(orderHistory.getStatus()));

        // Bắt sự kiện khi nhấn nút btnOrderInfo
        btnOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến trang OrderDetailActivity
                Intent intent = new Intent(context, OrderDetailActivity.class);
                // Truyền dữ liệu cần thiết cho trang chi tiết đơn hàng
                intent.putExtra("orderCode", orderHistory.getOrderCode());
                intent.putExtra("totalAmount", orderHistory.getTotalAmount());
                intent.putExtra("foodList", (ArrayList<Food>) orderHistory.getFoodList());
                intent.putExtra("orderDate", orderHistory.getOrderDate().toString());
                intent.putExtra("userFullName", orderHistory.getUserFullName());
                intent.putExtra("phoneNumber", orderHistory.getPhoneNumber());
                intent.putExtra("address", orderHistory.getAddress());
                context.startActivity(intent);
            }
        });

        return convertView;
    }

}
