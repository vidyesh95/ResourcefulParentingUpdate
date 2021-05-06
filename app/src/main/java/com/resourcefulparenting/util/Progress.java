package com.resourcefulparenting.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;



public class Progress {

    private ProgressBar progressBar;
    private Activity activity;
    private Context context;
    private ViewGroup viewGroup;


    public Progress(Activity activity, Context context, ViewGroup viewGroup) {
        try {
            this.activity = activity;
            this.context = context;
            this.viewGroup = viewGroup;
            initProgressBar();
        } catch (Exception e) {
            //e.printStackTrace();();
        }
    }

    private void initProgressBar() {
        try {
            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            viewGroup.addView(progressBar, params);
//        progressBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
//        Circle circle = new Circle();
//        circle.setColor(ContextCompat.getColor(context, R.color.red_color));
//        progressBar.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent_black_with_opacity));
//        progressBar.setIndeterminateDrawable(circle);

            progressBar.getIndeterminateDrawable().setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.MULTIPLY);
            progressBar.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            //e.printStackTrace();();
        }
    }

    public void showProgresBar() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception ignored) {
        }
    }

    public void hideProgressBar() {
        try {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } catch (Exception ignored) {
        }
    }


}
