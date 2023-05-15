package com.example.finalproject.AdminActivity;

import static com.example.finalproject.UserActivity.MainActivity.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.Model.OrderHistory;
import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderListAdminFragment extends Fragment {
    private  OrderHistoryAdminAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_admin, container, false);

        ListView lvOrderHistoryAdmin = view.findViewById(R.id.lvOrderHistoryAdmin);
        adapter = new OrderHistoryAdminAdapter(getActivity(), R.layout.order_history_list, getOrderHistoryList());
        lvOrderHistoryAdmin.setAdapter(adapter);

        return view;
    }

    private List<OrderHistory> getOrderHistoryList() {
        List<OrderHistory> orderHistoryList = new ArrayList<>();

        DatabaseReference orderRef = database.getReference("orders");
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
        return orderHistoryList;
    }
}

