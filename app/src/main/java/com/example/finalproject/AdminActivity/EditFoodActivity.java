package com.example.finalproject.AdminActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Model.Food;
import com.example.finalproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditFoodActivity extends AppCompatActivity {
    EditText edtName;
    EditText edtPrice;
    EditText edtImg;
    Button btnExit;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_food);
        edtName = findViewById(R.id.edtFoodName);
        edtPrice = findViewById(R.id.edtFoodPrice);
        edtImg= findViewById(R.id.edtFoodPicturePath);
        btnExit = findViewById(R.id.btnExit);
        btnUpdate = findViewById(R.id.btnUpdate);

        //lấy dữ liệu để xem lại trước khi chỉnh sửa
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position", 0);
        String name = bundle.getString("name");
        edtName.setText(name);
        int price = bundle.getInt("price");
        edtPrice.setText(String.valueOf(price));
        int img = bundle.getInt("img");
        edtImg.setText(String.valueOf(img));
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentResult = new Intent();

                setResult(RESULT_OK);

                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("list_foods");

                String nameNew = edtName.getText().toString().trim();
                String priceNew = edtPrice.getText().toString().trim();
                String imgNew = edtImg.getText().toString().trim();
                Food food = new Food(nameNew, Integer.parseInt(priceNew), Integer.parseInt(imgNew));
                myRef.child(String.valueOf(position)).setValue(food);
                Intent intentResult = new Intent();

                setResult(RESULT_OK);

                finish();
            }
        });
    }
}
