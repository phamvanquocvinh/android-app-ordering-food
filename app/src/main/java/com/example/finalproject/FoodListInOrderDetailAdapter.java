package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Model.Food;

import java.text.NumberFormat;
import java.util.List;

public class FoodListInOrderDetailAdapter extends ArrayAdapter<Food> {
    private Context context;
    private List<Food> foodList;

    public FoodListInOrderDetailAdapter(Context context, List<Food> foodList) {
        super(context, R.layout.foods_list_in_order_detail, foodList);
        this.context = context;
        this.foodList = foodList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.foods_list_in_order_detail, parent, false);

        // Lấy dữ liệu của một item trong danh sách
        Food food = getItem(position);

        // Ánh xạ dữ liệu vào các View trong item layout
        ImageView ivFoodImage = convertView.findViewById(R.id.ivFood);
        TextView tvFoodName = convertView.findViewById(R.id.tvFoodName);
        TextView tvFoodPrice = convertView.findViewById(R.id.tvFoodPrice);
        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);

        NumberFormat formatter = NumberFormat.getInstance();
        String formattedPrice = formatter.format(food.getPrice());
        tvFoodPrice.setText(formattedPrice);

        ivFoodImage.setImageResource(food.getImage());
        tvFoodName.setText(food.getName());
        tvQuantity.setText(String.valueOf(food.getQuantity()));

        // Trả về View đã ánh xạ dữ liệu
        return convertView;
    }
}
