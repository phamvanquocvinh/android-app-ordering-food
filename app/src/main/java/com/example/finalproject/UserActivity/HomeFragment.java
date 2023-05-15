package com.example.finalproject.UserActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.finalproject.Model.Food;
import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ListView listView;
    private List<Food> foodList;
    private FoodListAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = view.findViewById(R.id.lvFood);
        foodList = new ArrayList<>();

        // lấy dữ liệu vào danh sách
        addListFoodDefault();
        foodList.clear();
        getListFoodsFromRealtimeDatabase();

        // Thiết lập Adapter
        adapter = new FoodListAdapter(getActivity(), foodList);
        listView.setAdapter(adapter);
        return view;
    }

    private void getListFoodsFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_foods");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    foodList.add(food);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addListFoodDefault() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("list_foods");
        foodList.add(new Food("Cơm tấm", 30000, R.drawable.food_com_tam));
        foodList.add(new Food("Cơm gà", 35000, R.drawable.food_com_ga));
        foodList.add(new Food("Bún bò", 45000, R.drawable.food_bun_bo));
        foodList.add(new Food("Phở", 45000, R.drawable.food_pho));
        foodList.add(new Food("Bánh canh", 45000, R.drawable.food_banh_canh));
        foodList.add(new Food("Mì xào", 35000, R.drawable.food_mi_xao));
        foodList.add(new Food("Miếng trộn", 35000, R.drawable.food_mieng_tron));
        foodList.add(new Food("Cơm chiên", 40000, R.drawable.food_com_chien));
        foodList.add(new Food("Chả giò", 25000, R.drawable.food_cha_gio));
        foodList.add(new Food("Khoai tây chiên", 25000, R.drawable.food_khoai_tay_chien));
        myRef.setValue(foodList);
    }
}
