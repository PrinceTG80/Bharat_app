package com.example.bharat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView t_shirts, sports_t_shirts, female_dresses, sweather, glasses, purses_bags, hats, shoes, headphones, laptops, watches, mobile_phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        t_shirts =(ImageView) findViewById(R.id.t_shirts);
        sports_t_shirts =(ImageView) findViewById(R.id.sports_t_shirts);
        female_dresses =(ImageView) findViewById(R.id.female_dresses);
        sweather =(ImageView) findViewById(R.id.sweather);

        glasses =(ImageView) findViewById(R.id.glasses);
        purses_bags =(ImageView) findViewById(R.id.purses_bags);
        hats =(ImageView) findViewById(R.id.hats);
        shoes =(ImageView) findViewById(R.id.shoes);

        headphones =(ImageView) findViewById(R.id.headphones);
        laptops =(ImageView) findViewById(R.id.laptops);
        watches =(ImageView) findViewById(R.id.watches);
        mobile_phones =(ImageView) findViewById(R.id.mobile_phones);

        t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "t_shirts");
                startActivity(intent);
            }
        });

        sports_t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "sports_t_shirts");
                startActivity(intent);
            }
        });

        female_dresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "female_dresses");
                startActivity(intent);
            }
        });

        sweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "sweather");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "glasses");
                startActivity(intent);
            }
        });

        purses_bags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "purses_bags");
                startActivity(intent);
            }
        });

        hats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "hats");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "shoes");
                startActivity(intent);
            }
        });

        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "headphones");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "laptops");
                startActivity(intent);
            }
        });

        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "watches");
                startActivity(intent);
            }
        });

        mobile_phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProduct_Activity.class);
                intent.putExtra("Category", "mobile_phones");
                startActivity(intent);
            }
        });
    }
}