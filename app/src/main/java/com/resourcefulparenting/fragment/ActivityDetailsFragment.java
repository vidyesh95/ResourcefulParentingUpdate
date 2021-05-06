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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.adapter.AdapterImageListing;
import com.resourcefulparenting.databinding.FragmentActivityDetailsBinding;
import com.resourcefulparenting.models.ActivityDetailsResponse;
import com.resourcefulparenting.models.AcyivitySendComResponse;
import com.resourcefulparenting.models.AlarmResponse;
import com.resourcefulparenting.models.Input.ActivityDetailsCheck;
import com.resourcefulparenting.models.Input.ActivitySendCheck;
import com.resourcefulparenting.models.Input.AlarmCheck;
import com.resourcefulparenting.models.Input.ChildeImageCheck;
import com.resourcefulparenting.models.Input.VideoCheck;
import com.resourcefulparenting.models.ProfileImageResponse;
import com.resourcefulparenting.models.VideoResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.CheckPermission;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Logout;
import com.resourcefulparenting.util.Prefs;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDetailsFragment extends Fragment {
    final ActivitySendCheck activitySendCheck = new ActivitySendCheck();
    private FragmentActivityDetailsBinding binding;
    Dialog dialog;
    final VideoCheck videoCheck = new VideoCheck();
    private Context context;
    private String activity_id, child_id;
    ArrayList<String> images = new ArrayList<>();
    final ActivityDetailsCheck todaysactivityCheck = new ActivityDetailsCheck();
    final int FROM_GALLERY = 100;
    final int FROM_CAMERA = 200;
    String img_base64 = "";
    private CheckPermission checkPermission;
    AdapterImageListing adapterImageListing;
    final ChildeImageCheck childeImageCheck = new ChildeImageCheck();
    AsyncTask mMyTask;
    ProgressDialog mProgressDialog;
    URL url1;
    URL url2;
    URL url;
    final AlarmCheck alarmCheck = new AlarmCheck();
    Boolean isalarmset;

    public ActivityDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActivityDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = container.getContext();
        checkPermission = new CheckPermission(getActivity());
        activity_id = getArguments().getString("activity_id");
        child_id = getArguments().getString("child_id");
        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  H.T(context,activity_id);

        ImageView milestone = getActivity().findViewById(R.id.milestone_img);
        //   TextView tv_Resource =getActivity().findViewById(R.id.tv_Resource);
        //   tv_Resource.setVisibility(View.GONE);
        milestone.setVisibility(View.GONE);

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Parenting");
        mProgressDialog.setMessage("Please wait, we are downloading your image file...");

        checkNetWork();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rvList.setLayoutManager(mLayoutManager);

        binding.homeAlarm.setOnClickListener(view12 -> {
            checkNetWorkAlarm();
        });


        binding.homeCamera.setOnClickListener(view12 -> {

            if (images.size() > 3) {
                H.T(context, "Image Already Uploaded");
            } else {
                showCaptureDialog();
            }

        });

        binding.homeVideo.setOnClickListener(view12 -> {
            //  showVideoi();
            showpopupTitleDeedUpload();
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

            }
        });


        binding.btnRegister.setOnClickListener(view12 -> {
            checkNetWorkComplit();
        });
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
        todaysactivityCheck.activity_id = activity_id;
        Call<ActivityDetailsResponse> call = ApiClient.getRetrofit().create(Api.class).activityDetailsResponseCall(todaysactivityCheck);
        call.enqueue(new Callback<ActivityDetailsResponse>() {
            @Override
            public void onResponse(Call<ActivityDetailsResponse> call, Response<ActivityDetailsResponse> response) {
                binding.loading.setVisibility(View.GONE);
                H.L("responsk=" + new Gson().toJson(response.body()));
                ActivityDetailsResponse response1 = response.body();
                if (response1 != null) {
                    if (response1.error.equals("false")) {

                        binding.numOfPoints.setText(response1.activityinfo.activity_point);
                        binding.tvTargetActivity.setText(response1.activityinfo.category_name);
                        binding.tvActivityName.setText(response1.activityinfo.activity_name);
                        binding.edtDescription.setText(Html.fromHtml(response1.activityinfo.activity_description));
                        binding.edtLearning.setText(Html.fromHtml(response1.activityinfo.activity_learning));

                        if (response1.activityinfo.category_name.equalsIgnoreCase("komunikasi")) {
                            binding.targetIcon.setImageResource(R.drawable.language_white_icon);
                        } else if (response1.activityinfo.category_name.equalsIgnoreCase("angka-logika")) {
                            binding.targetIcon.setImageResource(R.drawable.logic_white_icon);
                        } else if (response1.activityinfo.category_name.equalsIgnoreCase("gerak")) {
                            binding.targetIcon.setImageResource(R.drawable.physical_white_icon);
                        } else if (response1.activityinfo.category_name.equalsIgnoreCase("diri")) {
                            binding.targetIcon.setImageResource(R.drawable.intrapersonal_white_icon);
                        } else if (response1.activityinfo.category_name.equalsIgnoreCase("sosial")) {
                            binding.targetIcon.setImageResource(R.drawable.interpersonal_white_icon);
                        } else if (response1.activityinfo.category_name.equalsIgnoreCase("visual")) {
                            binding.targetIcon.setImageResource(R.drawable.spatial_white_icon);
                        } else if (response1.activityinfo.category_name.equalsIgnoreCase("musik")) {
                            binding.targetIcon.setImageResource(R.drawable.music_white_icon);
                        } else if (response1.activityinfo.category_name.equalsIgnoreCase("lingkungan")) {
                            binding.targetIcon.setImageResource(R.drawable.environment_white_icon);
                        }

                        if (response1.activityinfo.isalarmset) {
                            isalarmset = false;
                            binding.homeAlarm.setImageDrawable(getResources().getDrawable(R.drawable.alarm_off));
                        } else {
                            isalarmset = true;
                            binding.homeAlarm.setImageDrawable(getResources().getDrawable(R.drawable.alarm));
                        }
                        if (response1.activityinfo.iscompleted) {
                            binding.btnRegister.setText(getResources().getString(R.string.do_it_again));
                        } else {
                            binding.btnRegister.setText(getResources().getString(R.string.we_did_it));
                        }
                        images.addAll(response1.activities_imgs);

                        if (images.size() == 2) {
                            binding.homeCamera.setVisibility(View.INVISIBLE);
                        } else {
                            binding.homeCamera.setVisibility(View.VISIBLE);
                        }
                        if (images.size() > 0) {
                            adapterImageListing = new AdapterImageListing(context, images, binding.homeCamera);
                            binding.rvList.setAdapter(adapterImageListing);
                        }

                     /*   binding.homeEdtDescription.setText(Html.fromHtml(response1.activitiesDetails.activity_description));
                        binding.edtLearning.setText(Html.fromHtml(response1.activitiesDetails.activity_learning));
                        activity_id=response1.activitiesDetails.activity_id;*/

                        if (!response1.activityinfo.activity_image.equals("") && !response1.activityinfo.activity_image.equals("null")) {
                            Glide.with(context)
                                    .load(response1.activityinfo.activity_image)
                                    .into(binding.activityImg);
                        }

                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ActivityDetailsResponse> call, Throwable t) {
            }
        });
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
                        if (images.size() == 2) {
                            binding.homeCamera.setVisibility(View.INVISIBLE);
                        } else {
                            binding.homeCamera.setVisibility(View.VISIBLE);
                        }
                        //  H.L("image-"+ response1.uploadedimg);
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
                String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        bitmap, "Title2", null);
                H.L("path" + path);
                Uri urluri = getImageUri(context, bitmap);
                imageUris.add(urluri);

            }

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_TEXT, "Hello");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            intent.setType("image/*");
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

    private void checkNetWorkAlarm() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                setAlram();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void setAlram() {
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
                if (response1 != null) {
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

            }

            @Override
            public void onFailure(Call<AlarmResponse> call, Throwable t) {
                binding.loading.setVisibility(View.GONE);
            }
        });
    }

    private void showpopupTitleDeedUpload() {

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
                    H.T(context, "Video url is not correct");
                } else {
                    checkNetWorkyoutube(video_url);
                    dialog.dismiss();
                }

            }
        });


        dialog.show();
    }

    private void checkNetWorkyoutube(String video_url) {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                youtubeurlsend(video_url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void youtubeurlsend(String video_url) {
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null;
    }
}