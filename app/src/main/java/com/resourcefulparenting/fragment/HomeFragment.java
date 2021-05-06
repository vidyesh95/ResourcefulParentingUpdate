package com.resourcefulparenting.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;
import com.resourcefulparenting.BuildConfig;
import com.resourcefulparenting.R;
import com.resourcefulparenting.activity.AddChildName;
import com.resourcefulparenting.activity.AddChildQuestions;
import com.resourcefulparenting.activity.ChildInstructions;
import com.resourcefulparenting.activity.QuestionsMilestonesActivity;
import com.resourcefulparenting.adapter.AdapterImageListing;
import com.resourcefulparenting.databinding.FragmentHomeBinding;
import com.resourcefulparenting.models.AcyivitySendComResponse;
import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.models.AddChild.GarphpointDetails;
import com.resourcefulparenting.models.AlarmResponse;
import com.resourcefulparenting.models.Input.ActivitySendCheck;
import com.resourcefulparenting.models.Input.AlarmCheck;
import com.resourcefulparenting.models.Input.ChildeImageCheck;
import com.resourcefulparenting.models.Input.TodaysactivityCheck;
import com.resourcefulparenting.models.Input.VideoCheck;
import com.resourcefulparenting.models.ProfileImageResponse;
import com.resourcefulparenting.models.TodayAcyivityResponse;
import com.resourcefulparenting.models.VideoResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.CheckPermission;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Logout;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.CirclePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    String activity_id;
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    Context context;
    int n = 1;
    ArrayList<PieEntry> pieEntries;
    private int[] MY_COLORS;
    private Spinner child;
    private ConstraintLayout target_Activity, show_activity;
    private ImageView plus;
    private boolean isvisible = false;
    Float point;
    Float point1, point2, point3, point4, point5, point6, point7, point8;

    boolean tutorialComplete1;
    SharedPreferences prefHome;
    SharedPreferences.Editor editor1;

    private int lang_color = 0, logic_color = 0, physical = 0, intrapersonal = 0, interpersonal = 0, spatial = 0, music = 0, environment = 0;
    final int FROM_GALLERY = 100;
    final int FROM_CAMERA = 200;
    String img_base64 = "";
    Dialog dialog;
    private CheckPermission checkPermission;
    private List<ChildDetails> childDetails1 = new ArrayList<>();
    private List<GarphpointDetails> garphpointDetails1 = new ArrayList<>();

    final TodaysactivityCheck todaysactivityCheck = new TodaysactivityCheck();
    final ActivitySendCheck activitySendCheck = new ActivitySendCheck();
    final AlarmCheck alarmCheck = new AlarmCheck();
    Boolean isalarmset;
    ArrayList<String> childs = new ArrayList<>();
    String child_id = "";

    final ChildeImageCheck childeImageCheck = new ChildeImageCheck();
    final VideoCheck videoCheck = new VideoCheck();
    AdapterImageListing adapterImageListing;
    TodayAcyivityResponse todayAcyivityResponse;
    ArrayList<String> images = new ArrayList<>();
    AsyncTask mMyTask;
    ProgressDialog mProgressDialog;
    String iconsStoragePath = Environment.getExternalStorageDirectory() + "/Parenting/";
    URL url1;
    URL url2;
    URL url;
    String firstid = "", secodid = "", thirdid = "";
    ArrayList<Integer> colors;

    private String  name, gender, date_, month_, year_;

    public HomeFragment() {
        // Required empty public constructor
    }
  /*  private final URL[] URLS = {stringToURL("https://resourcefulparenting.parkmapped.com/uploads/childactivityimg/130-4-1596190217.png"),
            stringToURL("https://resourcefulparenting.parkmapped.com/uploads/childactivityimg/130-4-1596180332.png")

    };*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        prefHome = getActivity().getSharedPreferences("MyPrefHome", MODE_PRIVATE);

        tutorialComplete1 = prefHome.getBoolean("key_name2", false);  // getting boolean

        if (!tutorialComplete1) {
            runTutorialSequence();
        }

        pieChart = binding.pieChart;
        child = binding.spinChildName;
        target_Activity = binding.showHideActivityDetails;
        show_activity = binding.homeActivity;
        context = container.getContext();
        //   login_token = getArguments().getString("login_token");
        return view;
    }

    private void runTutorialSequence() {
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(binding.targetHolder)
                .setPrimaryText("Aktivitas sehari-hari")
                .setSecondaryText("Ingin tahu aktifitas apa yang bisa Ayah Bunda lakukan bersama Si Kecil hari ini? Lihat rekomendasi aktifitas di sini.")
                .setBackgroundColour(getResources().getColor(R.color.light_green_transparent))
                .setPrimaryTextColour(getResources().getColor(R.color.white))
                .setSecondaryTextColour(getResources().getColor(R.color.yellow))
                /*.setBackButtonDismissEnabled(true)*/
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                /*.setFocalRadius(R.dimen.dp800)*/
                .setFocalPadding(R.dimen.dp80)
                /*.setTextPadding(R.dimen.dp100)*/
                .setPromptBackground(new CirclePromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                            // User has pressed the prompt target
                            runTutorialSequence2();
                        }
                    }
                })
                .show();
    }

    private void runTutorialSequence2() {
        final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        /*TapTarget.forView(binding.targetHolder, "Aktivitas sehari-hari", "Ingin tahu aktifitas apa yang bisa Ayah Bunda lakukan bersama Si Kecil hari ini? Lihat rekomendasi aktifitas di sini.")
                                .id(0)
                                // All options below are optional
                                .outerCircleColor(R.color.light_green)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.66f)            // Specify the alpha amount for the outer circle
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
                                .targetRadius(50),                  // Specify the target radius (in dp)*/

                        TapTarget.forView(binding.holder, "Tonggak sejarah", "Setiap anak tumbuh secara bertahap. Lengkapi milestone untuk melihat tahapan perkembangan Si Kecil.")
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
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(103),                  // Specify the target radius (in dp)

                        TapTarget.forView(binding.holder1, "Poin", "Dapatkan poin dengan melengkapi aktifitas dan milestones bersama Si Kecil.")
                                .id(2)
                                // All options below are optional
                                .outerCircleColor(R.color.physical)      // Specify a color for the outer circle
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
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(103)                  // Specify the target radius (in dp)
                )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                        editor1 = prefHome.edit();
                        editor1.putBoolean("key_name2", true);           // Saving boolean - true/false
                        editor1.apply();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // Perform action for the current target
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
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

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)*/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermission = new CheckPermission(getActivity());
        ImageView milestone = getActivity().findViewById(R.id.milestone_img);
        milestone.setVisibility(View.VISIBLE);

        // mImageView = findViewById(R.id.imageView);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Parenting");
        mProgressDialog.setMessage("Please wait, we are downloading your image file...");
        // getEntries();
        binding.btnMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(context, QuestionsMilestonesActivity.class);
                startActivity(mainIntent);
            }
        });

        binding.addChild.setOnClickListener(view12 -> {
            Intent add_child = new Intent(context, AddChildName.class);
            startActivity(add_child);
        });

        binding.homeShare.setOnClickListener(view12 -> {
            // mMyTask = new DownloadTask().execute(url1,url2);
            if (!images.isEmpty()) {
                if (images.size() == 2) {
                    url1 = stringToURL(images.get(0));
                    url2 = stringToURL(images.get(1));
                    mMyTask = new DownloadTask().execute(url1, url2);
                } else {
                    url1 = stringToURL(images.get(0));
                    mMyTask = new DownloadTask().execute(url1);
                }
            } else {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    String shareMessage = "Hey please find app link\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });
        binding.btnRegister.setOnClickListener(view12 -> {
            checkNetWorkComplit();
        });
        if (images.size() > 3) {
            binding.homeCamera.setVisibility(View.GONE);
        } else {
            binding.homeCamera.setVisibility(View.VISIBLE);
        }
        binding.homeCamera.setOnClickListener(view12 -> {
            if (images.size() > 3) {
                H.T(context, "Image Already Uploaded");
            } else {
                showCaptureDialog();
            }
            H.L("size;;;" + images.size());
        });
        binding.homeVideo.setOnClickListener(view12 -> {
            //  showVideoi();
            showPopupTitleDeedUpload();
        });
        binding.homeAlarm.setOnClickListener(view12 -> {
            checkNetWorkAlarm();
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rvList.setLayoutManager(mLayoutManager);
        try {
            childDetails1.clear();
            JSONArray jsonArray = new JSONArray(Prefs.getChildDetails(context));
            //  //Log.d("Arraym", String.valueOf(jsonArray.length()));
            for (int i = 0; i < jsonArray.length(); i++) {
                ChildDetails childDetails = new ChildDetails();
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("child_id");
                String name = object.getString("child_name");
                childDetails.setId(id);
                childDetails.setChild_name(name);
                childs.add(name);
                childDetails1.add(childDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_text, childs);
        // attaching data adapter to spinner
        child.setAdapter(dataAdapter);
        if (childs.size() == 1) {
            child.setEnabled(false);
            for (int j = 0; j < childDetails1.size(); j++) {
                child_id = childDetails1.get(j).getId();
                H.L("call" + childDetails1.get(j).getId());
                checkNetWork();
                Prefs.setChildID(context, child_id);
                break;
            }
        } else {
            n = 1;
            child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String text = parent.getItemAtPosition(pos).toString();
                    JSONArray jsonArray1 = new JSONArray();
                    for (int j = 0; j < childDetails1.size(); j++) {
                        JSONObject object = new JSONObject();
                        if (text.equalsIgnoreCase(childDetails1.get(j).getChild_name())) {
                            child_id = childDetails1.get(j).getId();
                            H.L("idd" + childDetails1.get(j).getId());
                            checkNetWork();
                            Prefs.setChildID(context, child_id);
                            try {
                                object.put("child_id", childDetails1.get(j).getId());
                                object.put("child_name", childDetails1.get(j).getChild_name());
                                jsonArray1.put(0, object);
                                // Prefs.setChildDetails(context, jsonArray1.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // break;
                        } else {
                            try {
                                object.put("child_id", childDetails1.get(j).getId());
                                object.put("child_name", childDetails1.get(j).getChild_name());
                                jsonArray1.put(n, object);
                                n++;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Prefs.setChildDetails(context, jsonArray1.toString());
                    n = 1;
                }

                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //todo
            name = getActivity().getIntent().getStringExtra("name");
            gender = getActivity().getIntent().getStringExtra("gender");
            date_ = getActivity().getIntent().getStringExtra("date");
            month_ = getActivity().getIntent().getStringExtra("month");
            year_ = getActivity().getIntent().getStringExtra("year");
        }

        target_Activity.setOnClickListener(view1 -> {
            if (!isvisible) {
                show_activity.setVisibility(View.VISIBLE);
                binding.minus.setImageResource(R.drawable.minus);
                isvisible = true;
            } else {
                show_activity.setVisibility(View.GONE);
                binding.minus.setImageResource(R.drawable.plus);
                isvisible = false;
            }
        });

        //todo
        binding.answerQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChildInstructions.class);
                intent.putExtra("name", name);
                intent.putExtra("gender", gender);
                intent.putExtra("date", date_);
                intent.putExtra("month", month_);
                intent.putExtra("year", year_);
                startActivity(intent);
            }
        });

        //todo
        binding.answerQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChildInstructions.class);
                startActivity(intent);
            }
        });
    }

    private void checkNetWork() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                getTodayActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void getTodayActivity() {
        images.clear();

        binding.loading.setVisibility(View.VISIBLE);
        todaysactivityCheck.login_token = Prefs.getLoginToken(getActivity());
        todaysactivityCheck.child_id = child_id;
        Call<TodayAcyivityResponse> call = ApiClient.getRetrofit().create(Api.class).getTodayActivity(todaysactivityCheck);
        call.enqueue(new Callback<TodayAcyivityResponse>() {
            @Override
            public void onResponse(Call<TodayAcyivityResponse> call, Response<TodayAcyivityResponse> response) {
                binding.loading.setVisibility(View.GONE);
                H.L("responsennnn=" + new Gson().toJson(response.body()));
                todayAcyivityResponse = response.body();
                if (todayAcyivityResponse != null && response.code() == 200) {
                    if (todayAcyivityResponse.error.equals("false")) {
                        binding.numOfActivityCompleted.setText(todayAcyivityResponse.total_activities_completed);
                        //  binding.numOfPointsEarned.setText(response1.badges);
                        binding.numOfPointsEarned.setText(todayAcyivityResponse.total_points);
                        if (todayAcyivityResponse.activitiesDetails != null) {
                            binding.tvTargetActivity.setText(todayAcyivityResponse.activitiesDetails.category_name);
                            binding.tvActivityName.setText(todayAcyivityResponse.activitiesDetails.activity_name);
                            binding.homeEdtDescription.setText(Html.fromHtml(todayAcyivityResponse.activitiesDetails.activity_description));
                            binding.edtLearning.setText(Html.fromHtml(todayAcyivityResponse.activitiesDetails.activity_learning));
                            if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("1")) {
                                binding.targetIcon.setImageResource(R.drawable.language_white_icon);
                            } else if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("2")) {
                                binding.targetIcon.setImageResource(R.drawable.logic_white_icon);
                            } else if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("3")) {
                                binding.targetIcon.setImageResource(R.drawable.physical_white_icon);
                            } else if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("4")) {
                                binding.targetIcon.setImageResource(R.drawable.intrapersonal_white_icon);
                            } else if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("5")) {
                                binding.targetIcon.setImageResource(R.drawable.interpersonal_white_icon);
                            } else if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("6")) {
                                binding.targetIcon.setImageResource(R.drawable.spatial_white_icon);
                            } else if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("7")) {
                                binding.targetIcon.setImageResource(R.drawable.music_white_icon);
                            } else if (todayAcyivityResponse.activitiesDetails.category_id.equalsIgnoreCase("8")) {
                                binding.targetIcon.setImageResource(R.drawable.environment_white_icon);
                            }
                            if (todayAcyivityResponse.activitiesDetails.iscompleted) {
                                binding.btnRegister.setText(getResources().getString(R.string.do_it_again));
                            } else {
                                binding.btnRegister.setText(getResources().getString(R.string.we_did_it));
                            }
                            activity_id = todayAcyivityResponse.activitiesDetails.activity_id;
                            images.addAll(todayAcyivityResponse.activities_imgs);
                            if (todayAcyivityResponse.activitiesDetails.isalarmset) {
                                isalarmset = false;
                                binding.homeAlarm.setImageDrawable(getResources().getDrawable(R.drawable.alarm_off));
                            } else {
                                isalarmset = true;
                                binding.homeAlarm.setImageDrawable(getResources().getDrawable(R.drawable.alarm));
                            }
                            //if true then reminder should be visible
                            //if false then reminder should be invisible
                            H.L("Milestone value " + todayAcyivityResponse.milestone.toString());
                            if (todayAcyivityResponse.milestone) {
                                H.L("Milestone value invisible" + todayAcyivityResponse.milestone.toString());
                                binding.milestoneLayout.setVisibility(View.VISIBLE);
                            } else {
                                H.L("Milestone value visible " + todayAcyivityResponse.milestone.toString());
                                binding.milestoneLayout.setVisibility(View.INVISIBLE);
                            }
                        }
                        garphpointDetails1.clear();
                        if (todayAcyivityResponse.activities_imgs.size() > 0) {
                            adapterImageListing = new AdapterImageListing(context, images, binding.homeCamera);
                            binding.rvList.setAdapter(adapterImageListing);
                        }
                        for (Object key : todayAcyivityResponse.graph_point.keySet()) {
                            GarphpointDetails garphpointDetails = new GarphpointDetails();
                            Double value = (Double) todayAcyivityResponse.graph_point.get(key);
                            point = value.floatValue();
                            garphpointDetails.setId(key.toString());
                            garphpointDetails.setValue(point);
                            garphpointDetails1.add(garphpointDetails);
                        }
                        print3largest(garphpointDetails1);
                        if (point == 0) {
                            pieChart.setVisibility(View.GONE);
                            //todo
                            binding.answerQuestions.setVisibility(View.VISIBLE);
                        } else {
                            binding.answerQuestions.setVisibility(View.GONE);

                            pieEntries = new ArrayList<>();
                            colors = new ArrayList<Integer>();
                            pieChart.setVisibility(View.VISIBLE);
                            for (int i = 0; i < garphpointDetails1.size(); i++) {
                                String id = garphpointDetails1.get(i).getId();
                                point = garphpointDetails1.get(i).getValue();
                                //   H.L("id"+point);
                                if (id.equalsIgnoreCase("1")) {
                                    //  pieEntries.add(new PieEntry(point,"", getResources().getDrawable(R.drawable.language_icon)));
                                    point1 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        lang_color = getResources().getColor(R.color.language_dark);
                                    } else {
                                        lang_color = getResources().getColor(R.color.language);
                                    }
                                } else if (id.equalsIgnoreCase("2")) {
                                    point2 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        logic_color = getResources().getColor(R.color.logic_dark);
                                    } else {
                                        logic_color = getResources().getColor(R.color.logic);
                                    }
                                } else if (id.equalsIgnoreCase("3")) {
                                    point3 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        physical = getResources().getColor(R.color.physical_dark);
                                    } else {
                                        physical = getResources().getColor(R.color.physical);
                                    }
                                } else if (id.equalsIgnoreCase("4")) {
                                    point4 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        intrapersonal = getResources().getColor(R.color.intrapersonal_dark);
                                    } else {
                                        intrapersonal = getResources().getColor(R.color.intrapersonal);
                                    }
                                } else if (id.equalsIgnoreCase("5")) {
                                    point5 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        interpersonal = getResources().getColor(R.color.interpersonal_dark);
                                    } else {
                                        interpersonal = getResources().getColor(R.color.interpersonal);
                                    }
                                } else if (id.equalsIgnoreCase("6")) {
                                    point6 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        spatial = getResources().getColor(R.color.spatial_dark);
                                    } else {
                                        spatial = getResources().getColor(R.color.spatial);
                                    }
                                } else if (id.equalsIgnoreCase("7")) {
                                    point7 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        music = getResources().getColor(R.color.music_dark);
                                    } else {
                                        music = getResources().getColor(R.color.music);
                                    }
                                } else if (id.equalsIgnoreCase("8")) {
                                    point8 = point;
                                    if (firstid.equalsIgnoreCase(id) || secodid.equalsIgnoreCase(id) || thirdid.equalsIgnoreCase(id)) {
                                        environment = getResources().getColor(R.color.environment_dark);
                                    } else {
                                        environment = getResources().getColor(R.color.environment);
                                    }
                                }
                            }

                            MY_COLORS = new int[]{lang_color, logic_color, physical, intrapersonal, interpersonal, spatial, music, environment};

                            for (int c : MY_COLORS) colors.add(c);
                            pieEntries = new ArrayList<>();
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.language_icon)));
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.logic_icon)));
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.physical_icon)));
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.intrapersonal_icon)));
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.interpersonal_icon)));
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.spatial_icon)));
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.music_icon)));
                            pieEntries.add(new PieEntry(3, "", getResources().getDrawable(R.drawable.environment_icon)));

                            pieDataSet = new PieDataSet(pieEntries, "");
                            pieData = new PieData(pieDataSet);
                            pieData.setValueFormatter(new PercentFormatter(pieChart));
                            pieChart.setData(pieData);
                            pieChart.setHoleRadius(70.0f);
                            pieChart.setTransparentCircleAlpha(0);

                            pieDataSet.setColors(colors);
                            pieChart.notifyDataSetChanged();
                            pieChart.invalidate();
                            pieDataSet.setValueTextColor(Color.BLACK);
                            pieDataSet.setValueTextSize(10f);
                            pieDataSet.setSelectionShift(0f);
                            pieChart.getData().setDrawValues(false);

                            pieChart.setRotationEnabled(false);

                            pieDataSet.setIconsOffset(new MPPointF(0, 50));

                            pieChart.getDescription().setText("stimulasi");
                            pieChart.setExtraOffsets(20, 0, 20, 0);
                            Legend legend = pieChart.getLegend();
                            legend.setEnabled(true);
                        }
                    }
                } else {
                    Logout.L(context);
                }
            }

            @Override
            public void onFailure(Call<TodayAcyivityResponse> call, Throwable t) {
                binding.loading.setVisibility(View.GONE);
            }
        });
    }

    private void checkNetWorkAlarm() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                setAlarm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }


    private void checkNetWorkComplit() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                setCompletedActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void setCompletedActivity() {
        binding.loading.setVisibility(View.VISIBLE);
        activitySendCheck.activity_id = activity_id;
        activitySendCheck.login_token = Prefs.getLoginToken(context);
        activitySendCheck.child_id = child_id;
        Call<AcyivitySendComResponse> call = ApiClient.getRetrofit().create(Api.class).seAcyivitySendComResponseCall(activitySendCheck);
        call.enqueue(new Callback<AcyivitySendComResponse>() {
            @Override
            public void onResponse(Call<AcyivitySendComResponse> call, Response<AcyivitySendComResponse> response) {
                binding.loading.setVisibility(View.GONE);
                AcyivitySendComResponse response1 = response.body();
                if (response1 != null) {
                    if (response1.error.equals("false")) {
                        H.T(context, response1.message);
                        binding.btnRegister.setText(getResources().getString(R.string.do_it_again));
                    } else {
                        H.T(context, response1.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AcyivitySendComResponse> call, Throwable t) {
                //  Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAlarm() {
        binding.loading.setVisibility(View.VISIBLE);
        alarmCheck.activity_id = activity_id;
        alarmCheck.login_token = Prefs.getLoginToken(context);
        alarmCheck.child_id = child_id;
        alarmCheck.is_alarm_set = isalarmset;
        Call<AlarmResponse> call = ApiClient.getRetrofit().create(Api.class).alarmResponse(alarmCheck);
        call.enqueue(new Callback<AlarmResponse>() {
            @Override
            public void onResponse(Call<AlarmResponse> call, Response<AlarmResponse> response) {
                binding.loading.setVisibility(View.GONE);
                AlarmResponse response1 = response.body();
                H.L("responsealram=" + new Gson().toJson(response.body()));
                if (response1.error.equals("false")) {

                    if (isalarmset) {
                        binding.homeAlarm.setImageDrawable(getResources().getDrawable(R.drawable.alarm_off));
                        isalarmset = false;
                    } else {
                        binding.homeAlarm.setImageDrawable(getResources().getDrawable(R.drawable.alarm));
                        isalarmset = true;
                    }
                    H.T(context, response1.message);
                } else {
                    H.T(context, response1.message);
                }
            }

            @Override
            public void onFailure(Call<AlarmResponse> call, Throwable t) {
                binding.loading.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == FROM_CAMERA) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //  addImage(bitmap);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                img_base64 = BitMapToString(resizedBitmap);
                checkNetWorkprofile();
            } else if (requestCode == FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
                try {
                    Uri selectedImage = data.getData();
                    InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                    //   addImage(bitmap);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                    img_base64 = BitMapToString(resizedBitmap);
                    checkNetWorkprofile();
                    H.L("img_base64" + img_base64);
                } catch (Exception e) {
                    //e.printStackTrace();();
                }
            }
        }

    }

    private void showCaptureDialog() {
        try {
            final CharSequence[] items = {getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.take_photo));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CheckPermission.STORAGE_PERMISSION_REQUEST_CODE);
                            } else {
                                openGallery();
                            }
                            break;

                        case 1:
                            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, CheckPermission.CAMERA_PERMISSION_REQUEST_CODE);
                            } else {
                                openCamera();
                            }
                            break;
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            //H.L(e.toString());
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), FROM_GALLERY);
    }

    private void openCamera() {
        try {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, FROM_CAMERA);
        } catch (Exception e) {
            //e.printStackTrace();();
        }

    }

    private String BitMapToString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
//String encodedImage = Base64.encode(b, Base64.DEFAULT);
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;

    }
 /*   @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        try {
            //H.L("onRequestPermissionsResult");
            switch (requestCode) {
                case CheckPermission.STORAGE_PERMISSION_REQUEST_CODE:
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openGallery();

                    } else {
                        // Helper.LOG("Permission rejected");
                    }

                    break;
                case CheckPermission.CAMERA_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        //  Helper.LOG("Permission rejected");
                    }
                    break;
            }
        } catch (Exception e) {
            //  Helper.LOG(e.toString());
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CheckPermission.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
                Toast.makeText(context, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CheckPermission.STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
                Toast.makeText(context, "Gallery permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Gallery permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addImage(Bitmap bitmap) {
        try {
            binding.homeImg.setImageBitmap(bitmap);
        } catch (Exception e) {
            //e.printStackTrace();();
        }
    }

    private void checkNetWorkprofile() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                imageprofile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void imageprofile() {
        binding.loading.setVisibility(View.VISIBLE);
        childeImageCheck.login_token = Prefs.getLoginToken(context);
        childeImageCheck.child_id = child_id;
        childeImageCheck.img_base64 = img_base64;
        childeImageCheck.activity_id = activity_id;
        Call<ProfileImageResponse> call = ApiClient.getRetrofit().create(Api.class).ImageResponse(childeImageCheck);
        call.enqueue(new Callback<ProfileImageResponse>() {
            @Override
            public void onResponse(Call<ProfileImageResponse> call, Response<ProfileImageResponse> response) {
                H.L("responseprofile=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                ProfileImageResponse response1 = response.body();
                if (response1 != null && response.code() == 200) {
                    if (response1.error.equals("false")) {
                        H.T(context, response1.message);
                        img_base64 = "";
                        H.L("image-" + response1.uploadedimg);
                        images.add(response1.uploadedimg);
                        adapterImageListing = new AdapterImageListing(context, images, binding.homeCamera);
                        binding.rvList.setAdapter(adapterImageListing);
                        adapterImageListing.notifyDataSetChanged();
                    } else {
                        H.T(context, response1.message);
                    }
                } else {
                    Logout.L(context);
                }
            }

            @Override
            public void onFailure(Call<ProfileImageResponse> call, Throwable t) {
                binding.loading.setVisibility(View.GONE);

            }
        });
    }

    private void showPopupTitleDeedUpload() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_video_upload);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);

        EditText etyoutube = dialog.findViewById(R.id.etyoutube);
        Button btnNo = dialog.findViewById(R.id.btnNo);
        Button btnconfrim = dialog.findViewById(R.id.btnconfrim);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnconfrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String video_url = etyoutube.getText().toString().trim();

                if (video_url.isEmpty()) {
                    H.T(context, "Masukkan alamat url Youtube");
                } else if (!isYoutubeUrl(video_url)) {
                    H.T(context, "Youtube Video url is not correct");
                } else {
                    checkNetWorkYoutube(video_url);
                    dialog.dismiss();
                }

            }
        });
        dialog.show();
    }

    private void checkNetWorkYoutube(String video_url) {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                youtubeUrlSend(video_url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void youtubeUrlSend(String video_url) {
        binding.loading.setVisibility(View.VISIBLE);
        videoCheck.login_token = Prefs.getLoginToken(context);
        videoCheck.child_id = child_id;
        videoCheck.video_url = video_url;
        videoCheck.activity_id = activity_id;
        Call<VideoResponse> call = ApiClient.getRetrofit().create(Api.class).videoResponse(videoCheck);
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                H.L("responseprofile=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                VideoResponse response1 = response.body();
                if (response1 != null && response.code() == 200) {
                    if (response1.error.equals("false")) {
                        H.T(context, response1.message);
                    } else {
                        H.T(context, response1.message);
                    }
                } else {
                    Logout.L(context);
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                binding.loading.setVisibility(View.GONE);
            }
        });
    }

    public static boolean isYoutubeUrl(String youTubeURl) {
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!youTubeURl.isEmpty() && youTubeURl.matches(pattern)) {
            success = true;
        } else {
            // Not Valid youtube URL
            success = false;
        }
        return success;
    }

    private class DownloadTask extends AsyncTask<URL, Integer, List<Bitmap>> {
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            mProgressDialog.show();

        }

        @Override
        protected List<Bitmap> doInBackground(URL... urls) {

            int count = urls.length;
            //URL url = urls[0];
            HttpURLConnection connection = null;
            List<Bitmap> bitmaps = new ArrayList<>();

            // Loop through the urls
            for (int i = 0; i < count; i++) {
                URL currentURL = urls[i];
                // So download the image from this url
                try {
                    // Initialize a new http url connection
                    connection = (HttpURLConnection) currentURL.openConnection();

                    // Connect the http url connection
                    connection.connect();

                    // Get the input stream from http url connection
                    InputStream inputStream = connection.getInputStream();

                    // Initialize a new BufferedInputStream from InputStream
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                    // Convert BufferedInputStream to Bitmap object
                    Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                    // Add the bitmap to list
                    bitmaps.add(bmp);
                    // add the url to list URL
                    // imageName.add(currentURL);

                    // Publish the async task progress
                    // Added 1, because index start from 0
                    publishProgress((int) (((i + 1) / (float) count) * 100));
                    if (isCancelled()) {
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Disconnect the http url connection
                    assert connection != null;
                    connection.disconnect();
                }
            }
            return bitmaps;
        }

        // When all async task done
        @SuppressLint("WrongThread")
        protected void onPostExecute(List<Bitmap> result) {
            // Hide the progress dialog
            mProgressDialog.dismiss();
            ArrayList<Uri> imageUris = new ArrayList<Uri>();

            for (int i = 0; i < result.size(); i++) {
                Bitmap bitmap = result.get(i);
                // Save the bitmap to internal storage
                //   Uri imageInternalUri = (bitmap, i);
                // Display the bitmap from memory
                //    addNewImageViewToLayout(bitmap);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                //  String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title2", null);

                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        bitmap, "Title2", null);
                H.L("path" + path);
                Uri urluri = getImageUri(context, bitmap);
                imageUris.add(urluri);
            }
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_TEXT, "Hey please find app link\n\n" + "https://play.google.com/store/apps/details?id=com.resourcefulparenting");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share with"));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title2", null);
        return Uri.parse(path);
    }

    protected URL stringToURL(String url1) {
        try {
            url = new URL(url1);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;
        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            // stream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return bitmap;
    }

    private InputStream getHttpConnection(String urlString)
            throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // binding = null;
    }

    private void print3largest(List<GarphpointDetails> garphpointDetails1) {
        float first, second, third;
        int i;
        third = first = second = Integer.MIN_VALUE;
        for (i = 0; i < garphpointDetails1.size(); i++) {
            /* If current element is greater than first*/
            if (garphpointDetails1.get(i).getValue() > first) {
                third = second;
                second = first;
                first = garphpointDetails1.get(i).getValue();
                firstid = garphpointDetails1.get(i).getId();
            }
            /* If arr[i] is in between first and second then update second  */
            else if (garphpointDetails1.get(i).getValue() > second) {
                third = second;
                second = garphpointDetails1.get(i).getValue();
                secodid = garphpointDetails1.get(i).getId();
                // id=garphpointDetails1.get(i).getId();
            } else if (garphpointDetails1.get(i).getValue() > third) {
                third = garphpointDetails1.get(i).getValue();
                thirdid = garphpointDetails1.get(i).getId();

            }
            //   H.L("sad"+id);
            //  break;
        }
        H.L("id" + firstid);
        H.L("id" + secodid);
        H.L("id" + thirdid);
    }
}