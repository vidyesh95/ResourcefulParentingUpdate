package com.resourcefulparenting.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.databinding.FragmentChangeProfileBinding;
import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.models.ChildDetailsResponse;
import com.resourcefulparenting.models.Input.ChildeImageCheck;
import com.resourcefulparenting.models.Input.DeleteChildCheck;
import com.resourcefulparenting.models.Input.UpdatechildCheck;
import com.resourcefulparenting.models.ProfileImageResponse;
import com.resourcefulparenting.models.Register.AcivityChildUpdate;
import com.resourcefulparenting.models.Register.ChildDeleteResponse;
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfileFragment extends Fragment {


    private FragmentChangeProfileBinding binding;
    final UpdatechildCheck registerCheck = new UpdatechildCheck();
    final DeleteChildCheck deleteChildCheck = new DeleteChildCheck();
    final ChildeImageCheck childeImageCheck = new ChildeImageCheck();
    private String child_id;
    private Context context;
    private String name, date, month, year, hight, weight;
    Button btn_save_changes_profile, btn_delete_profile;
    private CheckPermission checkPermission;
    CircleImageView profile_image;
    MaterialButton btn_change_profile;

    final int FROM_GALLERY = 100;
    final int FROM_CAMERA = 200;
    String img_base64 = "";
    private List<ChildDetails> childDetails1 = new ArrayList<>();

    public ChangeProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangeProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = container.getContext();
        child_id = getArguments().getString("child_id");
        btn_save_changes_profile = view.findViewById(R.id.btn_save_changes_profile);
        btn_delete_profile = view.findViewById(R.id.btn_delete_profile);
        profile_image = view.findViewById(R.id.profile_image);
        btn_change_profile = view.findViewById(R.id.btn_change_profile);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermission = new CheckPermission(getActivity());
        try {
            childDetails1.clear();
            JSONArray jsonArray = new JSONArray(Prefs.getChildDetails(context));
            for (int i = 0; i < jsonArray.length(); i++) {
                ChildDetails childDetails = new ChildDetails();
                JSONObject object = jsonArray.getJSONObject(i);
                String id = object.getString("child_id");
                String name = object.getString("child_name");
                childDetails.setId(id);
                childDetails.setChild_name(name);
                childDetails1.add(childDetails);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (childDetails1.size() == 1) {
            btn_delete_profile.setVisibility(View.GONE);
        } else {
            btn_delete_profile.setVisibility(View.VISIBLE);
        }

        checkNetWor1k();
        btn_save_changes_profile.setOnClickListener(view1 -> {
            //  H.T(context,child_id);
            validation();
        });
        btn_delete_profile.setOnClickListener(view1 -> {
            checkNetWorkDelete();
        });

        profile_image.setOnClickListener(view1 -> {
            showCaptureDialog();
        });
        btn_change_profile.setOnClickListener(view1 -> {
            showCaptureDialog();
        });
    }

    private void showCaptureDialog() {
        try {
            final CharSequence[] items = {getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getResources().getString(R.string.take_photo));
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    switch (item) {
                        case 0:
                            if (CheckPermission.checkDeviceOS()) {
                                if (checkPermission.checkStoragePermission()) {
                                    openGallery();
                                }
                            } else {
                                openGallery();
                            }
                            break;
                        case 1:
                            if (CheckPermission.checkDeviceOS()) {
                                if (checkPermission.checkCameraPermission()) {
                                    openCamera();
                                }
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

    private void openCamera() {
        try {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, FROM_CAMERA);

        } catch (Exception e) {
            //e.printStackTrace();();
        }
    }

    private void validation() {
        name = binding.editName.getText().toString();
        date = binding.editDate.getText().toString();
        month = binding.editMonth.getText().toString();
        year = binding.editYear.getText().toString();
        hight = binding.editHight.getText().toString();
        weight = binding.editWegiht.getText().toString();

        registerCheck.login_token = Prefs.getLoginToken(context);
        registerCheck.child_name = name;
        registerCheck.child_birth_date = date;
        registerCheck.child_birth_month = month;
        registerCheck.child_birth_year = year;
        registerCheck.child_height = hight;
        registerCheck.child_weight = weight;
        registerCheck.child_id = child_id;

        if (name.isEmpty()) {
            binding.edtChildNameProfile.setError("Masukkan nama anak");
        } else {
            checkNetWork();
        }
    }

    private void checkNetWork() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                sendProfileInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
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

    private void checkNetWorkDelete() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void delete() {
        binding.loading.setVisibility(View.VISIBLE);
        deleteChildCheck.login_token = Prefs.getLoginToken(context);
        deleteChildCheck.child_id = child_id;
        Call<ChildDeleteResponse> call = ApiClient.getRetrofit().create(Api.class).deleteResponseCall(deleteChildCheck);
        call.enqueue(new Callback<ChildDeleteResponse>() {
            @Override
            public void onResponse(Call<ChildDeleteResponse> call, Response<ChildDeleteResponse> response) {
                H.L("response=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                ChildDeleteResponse response1 = response.body();
                if (response1 != null && response.code() == 200) {
                    if (response1.error.equals("false")) {
                        JSONArray jsonArray1 = new JSONArray();
                        for (int j = 0; j < childDetails1.size(); j++) {
                            if (child_id.equalsIgnoreCase(childDetails1.get(j).getId())) {
                                // child_id = childDetails1.get(j).getId();
                                //  break;

                            } else {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("child_id", childDetails1.get(j).getId());
                                    object.put("child_name", childDetails1.get(j).getChild_name());
                                    jsonArray1.put(object);
                                    //Log.d("data", jsonArray1.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Prefs.setChildDetails(context, jsonArray1.toString());
                        getActivity().getSupportFragmentManager().popBackStack();
                        H.T(context, response1.message);
                    } else {
                        H.T(context, response1.message);
                    }
                } else {
                    Logout.L(context);
                }
            }

            @Override
            public void onFailure(Call<ChildDeleteResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendProfileInfo() {
        binding.loading.setVisibility(View.VISIBLE);
        Call<AcivityChildUpdate> call = ApiClient.getRetrofit().create(Api.class).updateResponseCall(registerCheck);
        call.enqueue(new Callback<AcivityChildUpdate>() {
            @Override
            public void onResponse(Call<AcivityChildUpdate> call, Response<AcivityChildUpdate> response) {
                H.L("response=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                AcivityChildUpdate response1 = response.body();
                if (response1 != null) {
                    if (response1.error.equals("false")) {
                        JSONArray jsonArray1 = new JSONArray();
                        for (int j = 0; j < childDetails1.size(); j++) {
                            if (child_id.equalsIgnoreCase(childDetails1.get(j).getId())) {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("child_id", childDetails1.get(j).getId());
                                    object.put("child_name", name);
                                    jsonArray1.put(object);
                                    //Log.d("data", jsonArray1.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("child_id", childDetails1.get(j).getId());
                                    object.put("child_name", childDetails1.get(j).getChild_name());
                                    jsonArray1.put(object);
                                    //Log.d("data", jsonArray1.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Prefs.setChildDetails(context, jsonArray1.toString());
                        getActivity().getSupportFragmentManager().popBackStack();
                        H.T(context, response1.message);
                    } else {
                        H.T(context, response1.message);
                    }
                }
            }

            @Override
            public void onFailure(Call<AcivityChildUpdate> call, Throwable t) {

                // Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkNetWor1k() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                getProfileInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, getString(R.string.no_internet_connection));
        }
    }

    private void getProfileInfo() {
        binding.loading.setVisibility(View.VISIBLE);
        String url = "https://rpi-app.com/api/getchildinfo/" + child_id;
        Call<ChildDetailsResponse> call = ApiClient.getRetrofit().create(Api.class).childDetailsResponseCall(url);
        call.enqueue(new Callback<ChildDetailsResponse>() {
            @Override
            public void onResponse(Call<ChildDetailsResponse> call, Response<ChildDetailsResponse> response) {
                H.L("response=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                ChildDetailsResponse response1 = response.body();
                if (response1.error.equals("false")) {
                    // H.T(context,response1.message);
                    binding.editName.setText(response1.childinfo.child_name);
                    binding.editDate.setText(response1.childinfo.child_birth_date);
                    binding.editMonth.setText(response1.childinfo.child_birth_month);
                    binding.editYear.setText(response1.childinfo.child_birth_year);
                    binding.editHight.setText(response1.childinfo.height);
                    binding.editWegiht.setText(response1.childinfo.weight);
                    if (!response1.childinfo.profile_pic.equals("") && !response1.childinfo.profile_pic.equals("null") && !response1.childinfo.profile_pic.equalsIgnoreCase("https://resourcefulparenting.parkmapped.com/uploads/childprofile/")) {
                        Glide.with(context)
                                .load(response1.childinfo.profile_pic)
                                .into(profile_image);

                    } else {
                        Glide.with(context)
                                .load(R.drawable.change_profile)
                                .into(profile_image);
                    }


                } else {
                    //  H.T(context,response1.message);
                }
            }

            @Override
            public void onFailure(Call<ChildDetailsResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), FROM_GALLERY);
    }

    private void imageprofile() {
        binding.loading.setVisibility(View.VISIBLE);
        childeImageCheck.login_token = Prefs.getLoginToken(context);
        childeImageCheck.child_id = child_id;
        childeImageCheck.img_base64 = img_base64;
        Call<ProfileImageResponse> call = ApiClient.getRetrofit().create(Api.class).profileImageResponse(childeImageCheck);
        call.enqueue(new Callback<ProfileImageResponse>() {
            @Override
            public void onResponse(Call<ProfileImageResponse> call, Response<ProfileImageResponse> response) {
                H.L("responseprofile=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                ProfileImageResponse response1 = response.body();
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
            public void onFailure(Call<ProfileImageResponse> call, Throwable t) {
                binding.loading.setVisibility(View.GONE);

            }
        });
    }


    @Override
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FROM_CAMERA) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            addImage(bitmap);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
            img_base64 = BitMapToString(resizedBitmap);
            checkNetWorkprofile();

        } else if (requestCode == FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                addImage(bitmap);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
                img_base64 = BitMapToString(resizedBitmap);
                checkNetWorkprofile();
                H.L("img_base64" + img_base64);
            } catch (Exception e) {
                //e.printStackTrace();();
            }
        }


    }

    private void addImage(Bitmap bitmap) {
        try {
            profile_image.setImageBitmap(bitmap);
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
    public void onDestroyView() {
        super.onDestroyView();
        //  binding = null;
    }
}