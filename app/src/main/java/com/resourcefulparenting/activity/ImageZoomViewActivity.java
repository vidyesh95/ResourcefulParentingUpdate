package com.resourcefulparenting.activity;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.resourcefulparenting.R;
import com.resourcefulparenting.util.TouchImageView;

public class ImageZoomViewActivity extends AppCompatActivity {
    TouchImageView imageView;

    String image;
    Context context;
    LinearLayout llMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom_view);
        context=this;
        image = getIntent().getStringExtra("image");
        //H.L("Url1" + image);
        imageView = findViewById(R.id.image_zoom);
        llMenu = findViewById(R.id.llMenu);


        try {

            Glide.with(context)
                    .load(image)
                    .into(imageView);
        } catch (Exception e) {
            //e.printStackTrace();();
            //H.L("Ex-" + e.toString());
        }


        llMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

/*
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
*/
    }
}
