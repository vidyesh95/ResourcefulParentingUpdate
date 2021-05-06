package com.resourcefulparenting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.resourcefulparenting.R;
import com.resourcefulparenting.util.Prefs;

public class AddChildName extends AppCompatActivity {

    private Context context;
    private Button get_started;
    private CardView selected_boy, selected_girl;
    private ImageView img_boy, img_girl;
    private String gender = "Male";
    private String  child_name;
    private TextInputLayout edt_child_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_name);
        context = this;
      //  Prefs.setCurrentActivity(context, Prefs.CurrentActivity.ADDCHILDNAME);
        ids();

        img_boy.setOnClickListener(v -> {
            selected_boy.setVisibility(View.VISIBLE);
            selected_girl.setVisibility(View.GONE);
            gender = "Male";
        });

        img_girl.setOnClickListener(v -> {
            selected_girl.setVisibility(View.VISIBLE);
            selected_boy.setVisibility(View.GONE);
            gender = "Female";
        });

        get_started.setOnClickListener(v -> {
            child_name = edt_child_name.getEditText().getText().toString();

            if(child_name.isEmpty()){
                edt_child_name.setError("Masukkan nama anak");
            }else {
                edt_child_name.setError(null);
                Intent next = new Intent(context, AddChildAge.class);
                next.putExtra("child_name", child_name);
                next.putExtra("gender", gender);
                startActivity(next);
                finish();
            }
        });
    }

    private void ids() {
        get_started = findViewById(R.id.btn_getStarted);
        selected_boy = findViewById(R.id.selected_card_boy);
        selected_girl = findViewById(R.id.selected_card_girl);
        img_boy = findViewById(R.id.img_boy);
        img_girl = findViewById(R.id.img_girl);
        edt_child_name = findViewById(R.id.edt_child_name);
    }

/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home_page = new Intent(context, MainActivity.class);
        startActivity(home_page);
    }
*/
}