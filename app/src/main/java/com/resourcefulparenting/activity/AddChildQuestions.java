package com.resourcefulparenting.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.models.AddChild.AddChildCheck;
import com.resourcefulparenting.models.AddChild.AddChildResponse;
import com.resourcefulparenting.models.QueriesResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChildQuestions extends AppCompatActivity {

    private Context context;
    private String login_token, name, gender, date_, month_, year_;
    String points = "0";
    // private boolean click = true;
    TextView question, tv_total;
    Button btn1, btn2, btn3, btn4, btn5, next, back;
    private RelativeLayout loading;
    private int current_question = 0;
    ImageView img_back;
    MaterialButton materialButton;

    public boolean tutorialCompleteQ;
    SharedPreferences prefQ;
    SharedPreferences.Editor editorQ;

    ArrayList<QueriesResponse.Query> result;
    final AddChildCheck addChildCheck = new AddChildCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_questions);
        context = this;
        //  Prefs.setCurrentActivity(context, Prefs.CurrentActivity.ADDCHILDNAME);
        loading = findViewById(R.id.loading);

        materialButton = findViewById(R.id.skip_questionnaire_button);

  /*     SharedPreferences settings=getSharedPreferences("prefs",0);
        login_token = settings.getString("login_token", login_token);*/

        name = getIntent().getStringExtra("name");
        gender = getIntent().getStringExtra("gender");
        date_ = getIntent().getStringExtra("date");
        month_ = getIntent().getStringExtra("month");
        year_ = getIntent().getStringExtra("year");
 /*       login_token = getIntent().getStringExtra("login_token");

        Toast.makeText(context, login_token , Toast.LENGTH_SHORT).show();*/
        bindView();

        // //Log.d("LinkedHaspMap", Main.main();)

        checkNetWork();

        prefQ = getApplicationContext().getSharedPreferences("MyPrefQ", MODE_PRIVATE);

        tutorialCompleteQ = prefQ.getBoolean("key_name5", false);  // getting boolean

        if (!tutorialCompleteQ) {
            runTutorialSequence();
        }

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddChildQuestions.this, MainActivity.class);
                checkNetWorkAddChild();
                startActivity(intent);
            }
        });
    }

    private void runTutorialSequence() {
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.inside_tv_question), "Pertanyaan", "Isi kuesioner berikut untuk mendapatkan 3 area kekuatan Si Kecil! Gunakan informasi ini untuk menstimulasi Si Kecil secara utuh.")
                                .id(0)
                                // All options below are optional
                                .outerCircleColor(R.color.light_green)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.56f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(40)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.yellow)  // Specify the color of the description text
                                //.textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(200),                  // Specify the target radius (in dp)

                        TapTarget.forView(findViewById(R.id.inside_rate_3), "Peringkat", "Beri nilai pada anak anda pada skala 5")
                                .id(1)
                                // All options below are optional
                                .outerCircleColor(R.color.orange)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.56f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(40)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(20)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.yellow)  // Specify the color of the description text
                                //.textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
                                //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(130)                  // Specify the target radius (in dp)

                )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                        editorQ = prefQ.edit();
                        editorQ.putBoolean("key_name5", true);           // Saving boolean - true/false
                        editorQ.apply();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // Perform action for the current target
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        final AlertDialog dialog = new AlertDialog.Builder(AddChildQuestions.this)
                                .setTitle("Uh oh")
                                .setMessage("You canceled the sequence")
                                .setPositiveButton("Oops", null).show();
                        TapTargetView.showFor(dialog,
                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
                                        .cancelable(false)
                                        .tintTarget(false), new TapTargetView.Listener() {
                                    @Override
                                    public void onTargetClick(TapTargetView view) {
                                        super.onTargetClick(view);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
        sequence.start();
    }

    private void bindView() {
        img_back = findViewById(R.id.img_back);
        question = findViewById(R.id.inside_tv_question);
        tv_total = findViewById(R.id.tv_total);
        next = findViewById(R.id.inside_next);
        back = findViewById(R.id.inside_back);
        //  submit = findViewById(R.id.inside_submit);
        btn1 = findViewById(R.id.inside_rate_1);
        btn2 = findViewById(R.id.inside_rate_2);
        btn3 = findViewById(R.id.inside_rate_3);
        btn4 = findViewById(R.id.inside_rate_4);
        btn5 = findViewById(R.id.inside_rate_5);


        btn1.setOnClickListener(v -> {
            points = "1";
            btn1.setBackgroundResource(R.drawable.orange_bg);
            btn2.setBackgroundResource(R.drawable.blue_bg);
            btn3.setBackgroundResource(R.drawable.blue_bg);
            btn4.setBackgroundResource(R.drawable.blue_bg);
            btn5.setBackgroundResource(R.drawable.blue_bg);
            result.get(current_question).is_check = true;
            result.get(current_question).points = points;

        });

        btn2.setOnClickListener(v -> {
            points = "2";
            btn2.setBackgroundResource(R.drawable.orange_bg);
            btn1.setBackgroundResource(R.drawable.blue_bg);
            btn3.setBackgroundResource(R.drawable.blue_bg);
            btn4.setBackgroundResource(R.drawable.blue_bg);
            btn5.setBackgroundResource(R.drawable.blue_bg);
            result.get(current_question).is_check = true;
            result.get(current_question).points = points;
        });

        btn3.setOnClickListener(v -> {
            points = "3";
            btn3.setBackgroundResource(R.drawable.orange_bg);
            btn2.setBackgroundResource(R.drawable.blue_bg);
            btn1.setBackgroundResource(R.drawable.blue_bg);
            btn4.setBackgroundResource(R.drawable.blue_bg);
            btn5.setBackgroundResource(R.drawable.blue_bg);
            result.get(current_question).points = points;
            result.get(current_question).is_check = true;
        });

        btn4.setOnClickListener(v -> {
            points = "4";
            btn4.setBackgroundResource(R.drawable.orange_bg);
            btn2.setBackgroundResource(R.drawable.blue_bg);
            btn3.setBackgroundResource(R.drawable.blue_bg);
            btn1.setBackgroundResource(R.drawable.blue_bg);
            btn5.setBackgroundResource(R.drawable.blue_bg);
            result.get(current_question).points = points;
            result.get(current_question).is_check = true;
        });

        btn5.setOnClickListener(v -> {
            points = "5";
            btn5.setBackgroundResource(R.drawable.orange_bg);
            btn2.setBackgroundResource(R.drawable.blue_bg);
            btn3.setBackgroundResource(R.drawable.blue_bg);
            btn4.setBackgroundResource(R.drawable.blue_bg);
            btn1.setBackgroundResource(R.drawable.blue_bg);
            result.get(current_question).is_check = true;
            result.get(current_question).points = points;
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_question < (result.size() - 1)) {
                    if (result.get(current_question).is_check) {

                        current_question++;
                        setQuestion(current_question);
                        btn4.setBackgroundResource(R.drawable.blue_bg);
                        btn2.setBackgroundResource(R.drawable.blue_bg);
                        btn3.setBackgroundResource(R.drawable.blue_bg);
                        btn1.setBackgroundResource(R.drawable.blue_bg);
                        btn5.setBackgroundResource(R.drawable.blue_bg);
                    } else {
                        H.T(context, "Pilih Poin");
                    }
                } else {

                    next.setText(getResources().getString(R.string.submit));
                    //    if (click) {
                    checkNetWorkAddChild();
                    //todo
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (current_question == 0) {

                } else {
                    next.setText(getResources().getString(R.string.next));
                    current_question--;
                    setQuestion(current_question);
                }
            }
        });
    }

    private void checkNetWork() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                getQuestions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }


    private void getQuestions() {
        loading.setVisibility(View.VISIBLE);
        Call<QueriesResponse> call = ApiClient.getRetrofit().create(Api.class).getQueries();
        call.enqueue(new Callback<QueriesResponse>() {
            @Override
            public void onResponse(Call<QueriesResponse> call, Response<QueriesResponse> response) {
                loading.setVisibility(View.GONE);
                H.L("response=" + new Gson().toJson(response.body()));
             /*   adapter = new TestAdapter(context, response.body(), AddChildQuestions.this);
                viewPager.setAdapter(adapter);*/
                //    viewPager.beginFakeDrag();
                QueriesResponse page_model = response.body();
                result = page_model.result_data;
                setQuestion(current_question);
            }

            @Override
            public void onFailure(Call<QueriesResponse> call, Throwable t) {
                //  Toast.makeText(AddChildQuestions.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setQuestion(int position) {
        if (position < result.size() && result.size() != 0) {
            tv_total.setText(current_question + 1 + "/" + result.size());
            setTextQuesion(Html.fromHtml(result.get(position).question) + "");
        }
    }

    public void setTextQuesion(String question1) {
        question.setText(question1);

/*
        if (result.get(current_question).points!=null)
        {
            if (result.get(current_question).points.equals("1"))
            {
                btn1.setBackgroundResource(R.drawable.orange_bg);

            }
             else if(result.get(current_question).points.equals("2"))
            {
                btn2.setBackgroundResource(R.drawable.orange_bg);

            }  else if(result.get(current_question).points.equals("3"))
            {
                btn3.setBackgroundResource(R.drawable.orange_bg);

            }   else if(result.get(current_question).points.equals("4"))
            {
                btn4.setBackgroundResource(R.drawable.orange_bg);

            }   else if(result.get(current_question).points.equals("5"))
            {
                btn5.setBackgroundResource(R.drawable.orange_bg);

            }

        }
*/

    }

    private void checkNetWorkAddChild() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                AddChild();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

  /*  private void AddChild(JSONArray jArry) {
        loading.setVisibility(View.VISIBLE);
        addChildCheck.child_name=name;
        addChildCheck.child_gender=gender;
        addChildCheck.child_birth_date=date_;
        addChildCheck.child_birth_month=month_;
        addChildCheck.child_birth_year=year_;
        addChildCheck.queries=jArry.toString();
        Call<AddChildResponse> call = ApiClient.getRetrofit().create(Api.class).AddChild(addChildCheck);
        call.enqueue(new Callback<AddChildResponse>() {
            @Override
            public void onResponse(Call<AddChildResponse> call, Response<AddChildResponse> response) {
              //  if(login_token.equals(addChildCheck.login_token)){
                H.L("responseadd=" + new Gson().toJson(response.body()));
                    loading.setVisibility(View.GONE);
                    Intent next = new Intent(context, MainActivity.class);
                    startActivity(next);
                    finish();
              //  }
            }

            @Override
            public void onFailure(Call<AddChildResponse> call, Throwable t) {

            }
        });
    }*/

    private void AddChild() {
        ArrayList<ArrayList<HashMap<String, String>>> hashMapArrayList1 = new ArrayList<>();
        ArrayList<HashMap<String, String>> hashMapArrayList2 = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            try {
                HashMap<String, String> hs = new HashMap<>();

                hs.put(result.get(i).id, "" + result.get(i).points);
                hashMapArrayList2.add(hs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        hashMapArrayList1.add(hashMapArrayList2);
      /*  JSONArray jArry = new JSONArray();
        for (int i = 0; i < result.size(); i++) {
            try {
                jObjd = new JSONObject();
                jObjd.put(result.get(i).id, result.get(i).points);
                jArry.put(jObjd);
                //  click=false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        loading.setVisibility(View.VISIBLE);
        addChildCheck.login_token = Prefs.getLoginToken(context);
        addChildCheck.child_name = name;
        addChildCheck.child_gender = gender;
        addChildCheck.child_birth_date = date_;
        addChildCheck.child_birth_month = month_;
        addChildCheck.child_birth_year = year_;
        addChildCheck.queries = hashMapArrayList2;
        H.L("Array:" + hashMapArrayList2);
        Call<AddChildResponse> call = ApiClient.getRetrofit().create(Api.class).AddChild(addChildCheck);
        call.enqueue(new Callback<AddChildResponse>() {
            @Override
            public void onResponse(Call<AddChildResponse> call, Response<AddChildResponse> response) {
                H.L("responsenana=" + new Gson().toJson(response.body()));
                loading.setVisibility(View.GONE);
                AddChildResponse addChildResponse = response.body();
                if (addChildResponse != null) {
                    if (addChildResponse.error.equals("false")) {
                        H.T(context, addChildResponse.message);
                        try {
                            JSONArray jsonArray1;
                            String oldjsonArray = Prefs.getChildDetails(context);
                            if (oldjsonArray.equalsIgnoreCase("")) {
                                jsonArray1 = new JSONArray();
                                //   H.T(context,"call1");
                            } else {
                                //   H.T(context,"call2");
                                jsonArray1 = new JSONArray(oldjsonArray);
                            }
                            JSONObject object = new JSONObject();
                            object.put("child_id", addChildResponse.childDetails.id);
                            object.put("child_name", name);
                            jsonArray1.put(object);
                            //  //Log.d("data",jsonArray1.toString());
                            Prefs.setChildDetails(context, jsonArray1.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent next = new Intent(context, MainActivity.class);
                        startActivity(next);
                        finish();

                    } else {
                        H.T(context, addChildResponse.message);

                    }
                }

            }

            @Override
            public void onFailure(Call<AddChildResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
            }
        });
    }

/*


    @Override
    public void submit(JSONArray items) {
        //Log.d("Main item", String.valueOf(items));

        addChildCheck.login_token = login_token;
        addChildCheck.child_name = name;
        addChildCheck.child_gender = gender;
        addChildCheck.child_birth_date = date_;
        addChildCheck.child_birth_month = month_;
        addChildCheck.child_birth_year = year_;
        addChildCheck.queries = queries;

        loading.setVisibility(View.VISIBLE);
        //getAddChild();
    }
*/
}