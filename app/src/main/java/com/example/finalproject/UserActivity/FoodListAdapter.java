package com.example.finalproject.UserActivity;

import static com.example.finalproject.UserActivity.CartFragment.cart;
import static com.example.finalproject.UserActivity.MainActivity.database;
import static com.example.finalproject.UserActivity.MainActivity.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalproject.Model.Food;
import com.example.finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;

public class FoodListAdapter extends ArrayAdapter<Food> {
    private Context context;
    private List<Food> foodList;

    public FoodListAdapter(Context context, List<Food> foodList) {
        super(context, R.layout.foods_list, foodList);
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.foods_list, parent, false);

        // Lấy dữ liệu của một item trong danh sách
        Food food = getItem(position);

        // Ánh xạ dữ liệu vào các View trong item layout
        ImageView ivFoodImage = convertView.findViewById(R.id.ivFood);
        TextView tvFoodName = convertView.findViewById(R.id.tvFoodName);
        TextView tvFoodPrice = convertView.findViewById(R.id.tvFoodPrice);
        Button btnDecrease = convertView.findViewById(R.id.btnDecrease);
        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
        Button btnIncrease = convertView.findViewById(R.id.btnIncrease);
        ImageButton btnAddToCart = convertView.findViewById(R.id.btnAddToCart);

        NumberFormat formatter = NumberFormat.getInstance();
        String formattedPrice = formatter.format(food.getPrice());
        tvFoodPrice.setText(formattedPrice);

        ivFoodImage.setImageResource(food.getImage());
        tvFoodName.setText(food.getName());
        tvQuantity.setText(String.valueOf(food.getQuantity()));

        //set listeners
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvQuantity.getText().toString().trim());
                count += 1;
                food.setQuantity(count);
                tvQuantity.setText(String.valueOf(food.getQuantity()));
            }
        });
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(tvQuantity.getText().toString().trim());
                count -= 1;
                if (count < 0) count = 0;
                food.setQuantity(count);
                tvQuantity.setText(String.valueOf(food.getQuantity()));
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickAddToCart(position);
            }
        });

        // Trả về View đã ánh xạ dữ liệu
        return convertView;
    }

    private void onclickAddToCart(int position) {
        Food food = foodList.get(position);
        if(food.getQuantity() == 0){
            return;
        }
        cart.add(food);
        //Toast.makeText(getContext(), food.toString(), Toast.LENGTH_SHORT).show();
        DatabaseReference myCartsRef = database.getReference("list_users/"+user.getUid()+"/carts");
        myCartsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myCartsRef.setValue(cart, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getContext(), "Đã thêm món ăn vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
