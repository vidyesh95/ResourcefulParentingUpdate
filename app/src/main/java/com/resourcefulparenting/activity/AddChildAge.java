package com.resourcefulparenting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.models.AddChild.AddChildCheck;
import com.resourcefulparenting.models.AddChild.AddChildResponse;
import com.resourcefulparenting.models.AddChild.Queries;
import com.resourcefulparenting.models.Register.RegisterResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChildAge extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Context context;
    private Button next;
    private Spinner date, month, year;
    private int date_;
    private int month_;
    private int year_;
    ArrayList<Integer> years;
    String login_token, name, gender, dates, months, yearss;
    final AddChildCheck addChildCheck = new AddChildCheck();
    private RelativeLayout loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_age);

        context = this;
        //  Prefs.setCurrentActivity(context, Prefs.CurrentActivity.ADDCHILDNAME);
        name = getIntent().getStringExtra("child_name");
        gender = getIntent().getStringExtra("gender");

        next = findViewById(R.id.btn_next);
        date = findViewById(R.id.date);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);
        loading = findViewById(R.id.loading);

        next.setOnClickListener(v -> {

            year_ = years.get(year.getSelectedItemPosition());
            month_ = Integer.parseInt(month.getItemAtPosition(month.getSelectedItemPosition()).toString());
            date_ = Integer.parseInt(date.getItemAtPosition(date.getSelectedItemPosition()).toString());
            int ages = Integer.parseInt(getAge(year_, month_, date_));
            dates = String.valueOf(date_);
            months = String.valueOf(month_);
            yearss = String.valueOf(year_);

            if (ages >= 2) {
                Intent next = new Intent(context, ChildInstructions.class);
                next.putExtra("name", name);
                next.putExtra("gender", gender);
                next.putExtra("date", dates);
                next.putExtra("month", months);
                next.putExtra("year", yearss);
                //  next.putExtra("login_token", login_token);
                startActivity(next);
                // finish();
            } else {
                H.L("Token" + Prefs.getLoginToken(context));
                // Toast.makeText(context, date_ + "/" + month_ + "/" + year_, Toast.LENGTH_SHORT).show();
                addChildCheck.login_token = Prefs.getLoginToken(context);
                addChildCheck.child_name = name;
                addChildCheck.child_gender = gender;
                addChildCheck.child_birth_date = dates;
                addChildCheck.child_birth_month = months;
                addChildCheck.child_birth_year = yearss;
                // addChildCheck.queries = qureies;
                checkNetWork();
                // getAddChild();
            }
        });

        years = new ArrayList<Integer>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int yearnew = thisYear - 6;
        for (int i = yearnew; i <= thisYear; i++) {
            years.add(i);
        }
        Comparator comparator = Collections.reverseOrder();
        Collections.sort(years, comparator);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, years);
        year.setAdapter(adapter);

        List<Integer> dates = new ArrayList<>();
        dates.add(1);
        dates.add(2);
        dates.add(3);
        dates.add(4);
        dates.add(5);
        dates.add(6);
        dates.add(7);
        dates.add(8);
        dates.add(9);
        dates.add(10);
        dates.add(11);
        dates.add(12);
        dates.add(13);
        dates.add(14);
        dates.add(15);
        dates.add(16);
        dates.add(17);
        dates.add(18);
        dates.add(19);
        dates.add(20);
        dates.add(21);
        dates.add(22);
        dates.add(23);
        dates.add(24);
        dates.add(25);
        dates.add(26);
        dates.add(27);
        dates.add(28);
        dates.add(29);
        dates.add(30);
        dates.add(31);

        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, dates);
        // attaching data adapter to spinner
        date.setAdapter(dataAdapter);

        List<Integer> months = new ArrayList<>();
        months.add(1);
        months.add(2);
        months.add(3);
        months.add(4);
        months.add(5);
        months.add(6);
        months.add(7);
        months.add(8);
        months.add(9);
        months.add(10);
        months.add(11);
        months.add(12);

        // Creating adapter for spinner
        ArrayAdapter<Integer> dataAdapter1 = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, months);
        // attaching data adapter to spinner
        month.setAdapter(dataAdapter1);

        //Log.d("Year", String.valueOf(year_));
        //Log.d("Month", String.valueOf(month_));
        //Log.d("Year", String.valueOf(date_));

    }

    private void checkNetWork() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                getAddChild();
            } catch (Exception e) {
                //e.printStackTrace();();
            }
        } else {
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void getAddChild() {
        loading.setVisibility(View.VISIBLE);
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
                            //Log.d("data",jsonArray1.toString());
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
    }

    public String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }
}
