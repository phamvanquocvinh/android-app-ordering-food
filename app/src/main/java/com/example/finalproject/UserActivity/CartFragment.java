package com.example.finalproject.UserActivity;

import static com.example.finalproject.UserActivity.MainActivity.database;
import static com.example.finalproject.UserActivity.MainActivity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.Model.Food;
import com.example.finalproject.Model.OrderHistory;
import com.example.finalproject.Model.User;
import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CartFragment extends Fragment {
    private ListView listView;
    private FoodListInCartAdapter adapter;
    public static int total;
    public static List<Food> cart = new ArrayList<>();
    public static TextView tvTotal;
    public static List<OrderHistory> list_orders= new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        listView = view.findViewById(R.id.lvFoodInCart);
        Button btnBook = view.findViewById(R.id.btnConfirm);
        tvTotal = view.findViewById(R.id.textView4);

        cart = new ArrayList<>();

        // Thêm dữ liệu vào danh sách
        getListFoodsInCartFromRealtimeDatabase();

        // Thiết lập Adapter
        adapter = new FoodListInCartAdapter(getActivity(), cart);
        listView.setAdapter(adapter);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBook();
            }
        });


        return view;
    }

    private void onClickBook() {
        DatabaseReference usersRef = database.getReference("list_users/" + user.getUid());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userGet = snapshot.getValue(User.class);
                Date now = new Date();
                int orderCode = new Random().nextInt(101);
                String newKey = String.valueOf(orderCode);
                OrderHistory order = new OrderHistory(orderCode, total, (ArrayList<Food>) cart, now,
                        userGet.getUserFullName(), userGet.getPhoneNumber(),
                        userGet.getAddress(), "Chưa nhận hàng");
                Toast.makeText(getActivity(),"Đặt hàng thành công",Toast.LENGTH_LONG).show();
                DatabaseReference myOrdersRef = database.getReference("list_users/" + user.getUid()+"/orders");
                myOrdersRef.child(newKey).setValue(order);
                //create order for admin to manage
                DatabaseReference ordersRef = database.getReference("orders");
                ordersRef.child(newKey).setValue(order);
                DatabaseReference cartRef = database.getReference("list_users/" + user.getUid() + "/carts");
                cartRef.removeValue();
                total = 0;
                cart.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getListFoodsInCartFromRealtimeDatabase() {
        DatabaseReference foodRef = database.getReference("list_users/" + user.getUid()+"/carts");


        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cart.clear();
                total = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    total += food.getQuantity() * food.getPrice();
                    cart.add(food);
                }
                NumberFormat formatter = NumberFormat.getInstance();
                String formattedPrice = formatter.format(total);
                tvTotal.setText(formattedPrice);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
