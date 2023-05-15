package com.example.finalproject.AdminActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalproject.Model.Food;
import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodListAdminAdapter extends ArrayAdapter<Food> {
    private Context context;
    private List<Food> foodList;
    private Fragment fragment;
    final static int REQUEST_CODE = 1;

    public FoodListAdminAdapter(Context context, List<Food> foodList, Fragment fragment) {
        super(context, R.layout.foods_list_admin, foodList);
        this.context = context;
        this.foodList = foodList;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.foods_list_admin, parent, false);

        Food food = foodList.get(position);

        // Ánh xạ dữ liệu vào các View trong item layout
        ImageView ivFoodImage = convertView.findViewById(R.id.ivFood);
        TextView tvFoodName = convertView.findViewById(R.id.tvFoodName);
        TextView tvFoodPrice = convertView.findViewById(R.id.tvFoodPrice);
        ImageButton btnUpdateFood = convertView.findViewById(R.id.btnUpdateFood);
        ImageButton btnDeleteFood = convertView.findViewById(R.id.btnDeleteFood);

        NumberFormat formatter = NumberFormat.getInstance();
        String formattedPrice = formatter.format(food.getPrice());
        tvFoodPrice.setText(formattedPrice);

        ivFoodImage.setImageResource(food.getImage());
        tvFoodName.setText(food.getName());

        btnUpdateFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến trang EditFoodActivity và truyền thông tin food để edit
                Intent intent = new Intent(fragment.getActivity(), EditFoodActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("name", food.getName());
                bundle.putInt("price", (int) food.getPrice());
                bundle.putInt("img", food.getImage());
                intent.putExtras(bundle);
                fragment.startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btnDeleteFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteFood(position);
            }
        });

        return convertView;
    }

    private void onClickDeleteFood(int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("VH-RESTAURANT")
                .setMessage("Bạn có chắc muốn xóa món ăn này?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference listFoodsRef = database.getReference("list_foods");

                        String index1 = String.valueOf(position);
                        String index2 = String.valueOf(foodList.size());
                        DatabaseReference object1Ref = listFoodsRef.child(index1);
                        DatabaseReference object2Ref = listFoodsRef.child(index2);

                        object1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Food object1 = snapshot.getValue(Food.class);

                                object2Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Food object2 = snapshot.getValue(Food.class);

                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put(index1, object2);
                                        updates.put(index2, object1);
                                        listFoodsRef.updateChildren(updates);

                                        listFoodsRef.child(index2).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(getContext(), "Xóa món ăn thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle the error
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error
                            }
                        });
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
