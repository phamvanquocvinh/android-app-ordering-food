package com.example.finalproject.AdminActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.Model.OrderHistory;
import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdminAdapter extends ArrayAdapter<OrderHistory> {

    private int layoutResource;

    public OrderHistoryAdminAdapter(@NonNull Context context, int resource, @NonNull List<OrderHistory> objects) {
        super(context, resource, objects);
        layoutResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layoutResource, null);
        }

        OrderHistory orderHistory = getItem(position);
        if (orderHistory != null) {
            TextView tvOrderID = convertView.findViewById(R.id.tvOrderID);
            TextView tvStatus = convertView.findViewById(R.id.tvStatus);

            // Thiết lập giá trị cho các View trong order_history_list.xml
            tvOrderID.setText(String.valueOf(orderHistory.getOrderCode()));
            tvStatus.setText(String.valueOf(orderHistory.getStatus()));
        }

        ImageButton btnOrderInfo = convertView.findViewById(R.id.btnOrderInfo);

        btnOrderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến trang OrderDetailAdminActivity
                Intent intent = new Intent(getContext(), OrderDetailAdminActivity.class);
                // Truyền dữ liệu cần thiết cho trang chi tiết đơn hàng admin
                intent.putExtra("orderCode", orderHistory.getOrderCode());
                intent.putExtra("totalAmount", orderHistory.getTotalAmount());
                intent.putExtra("foodList", (ArrayList) orderHistory.getFoodList());
                intent.putExtra("orderDate", orderHistory.getOrderDate().toString());
                intent.putExtra("userFullName", orderHistory.getUserFullName());
                intent.putExtra("phoneNumber", orderHistory.getPhoneNumber());
                intent.putExtra("address", orderHistory.getAddress());
                intent.putExtra("status", orderHistory.getStatus());
                getContext().startActivity(intent);
            }
        });


        return convertView;
    }
}
