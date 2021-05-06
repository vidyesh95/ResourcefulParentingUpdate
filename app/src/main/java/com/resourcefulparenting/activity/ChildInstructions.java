package com.resourcefulparenting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.resourcefulparenting.databinding.ActivityChildInstructionsBinding;

public class ChildInstructions extends AppCompatActivity {

    private String  name, gender, date_, month_, year_;
    private ActivityChildInstructionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildInstructionsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        name = getIntent().getStringExtra("name");
        gender = getIntent().getStringExtra("gender");
        date_ = getIntent().getStringExtra("date");
        month_ = getIntent().getStringExtra("month");
        year_ = getIntent().getStringExtra("year");

        binding.btnOk.setOnClickListener(v -> {
            Intent next = new Intent(this, AddChildQuestions.class);
            next.putExtra("name", name);
            next.putExtra("gender", gender);
            next.putExtra("date", date_);
            next.putExtra("month", month_);
            next.putExtra("year", year_);
            //  next.putExtra("login_token", login_token);
            startActivity(next);
          //  finish();
        });
        binding.imgBack.setOnClickListener(v -> finish());
    }
}