package com.resourcefulparenting.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.resourcefulparenting.R;
import com.resourcefulparenting.activity.AddChildName;
import com.resourcefulparenting.databinding.FragmentExplorerBinding;
import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ExplorerFragment extends Fragment {
    private FragmentExplorerBinding binding;
    private Context context;
    private Spinner child;
    int n = 1;
    private String name, activity_id, login_token;
    private List<ChildDetails> childDetails1 = new ArrayList<>();
    ArrayList<String> childs = new ArrayList<>();
    String child_id = "";

    boolean tutorialComplete2;
    SharedPreferences prefExplore;
    SharedPreferences.Editor editor2;

    public ExplorerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExplorerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = container.getContext();

        prefExplore = getActivity().getSharedPreferences("MyPrefExplore", MODE_PRIVATE);

        tutorialComplete2 = prefExplore.getBoolean("key_name3", false);  // getting boolean

        if (!tutorialComplete2) {
            runTutorialSequence();
        }

        return view;
    }

    private void runTutorialSequence() {
        final TapTargetSequence sequence = new TapTargetSequence(getActivity())
                .targets(
                        TapTarget.forView(binding.childName, "Pilih Anak", "Pilih Anak untuk 8 area pertumbuhan.")
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
                                .targetRadius(80),                  // Specify the target radius (in dp)

                        TapTarget.forView(binding.category, "Kategori", "Si Kecil memiliki 8 area pertumbuhan: Sosial, Diri, Komunikasi, Lingkungan, Visual, Angka-Logika, Musik, dan Gerak. Dapatkan ribuan aktifitas dalam semua kategori untuk membantu tumbuh kembang Si Kecil secara utuh.")
                                .id(0)
                                // All options below are optional
                                .outerCircleColor(R.color.light_green)      // Specify a color for the outer circle
                                .outerCircleAlpha(0.56f)            // Specify the alpha amount for the outer circle
                                .targetCircleColor(R.color.white)   // Specify a color for the target circle
                                .titleTextSize(34)                  // Specify the size (in sp) of the title text
                                .titleTextColor(R.color.white)      // Specify the color of the title text
                                .descriptionTextSize(18)            // Specify the size (in sp) of the description text
                                .descriptionTextColor(R.color.yellow)  // Specify the color of the description text
                                //.textColor(R.color.white)            // Specify a color for both the title and description text
                                .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                                .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                .drawShadow(true)                   // Whether to draw a drop shadow or not
                                .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                                .tintTarget(false)                   // Whether to tint the target view's color
                                .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                                //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                                .targetRadius(160)                  // Specify the target radius (in dp)
                )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                        editor2 = prefExplore.edit();
                        editor2.putBoolean("key_name3", true);           // Saving boolean - true/false
                        editor2.apply();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView milestone = getActivity().findViewById(R.id.milestone_img);
        milestone.setVisibility(View.GONE);

        binding.addChild.setOnClickListener(view12 -> {
            Intent add_child = new Intent(context, AddChildName.class);
            startActivity(add_child);
        });

        child = binding.spinChildName;

        binding.environment.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).environment();
            Prefs.setCategory_id(context, "8");
            Prefs.setChildID(context, child_id);
            name = "environment";
        });

        binding.physical.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).physical();
            name = "physical";
            Prefs.setCategory_id(context, "3");
            Prefs.setChildID(context, child_id);
        });

        binding.logic.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).logic();
            name = "logic";
            Prefs.setCategory_id(context, "2");
            Prefs.setChildID(context, child_id);
        });

        binding.language.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).language();
            name = "language";
            Prefs.setCategory_id(context, "1");
            Prefs.setChildID(context, child_id);

        });

        binding.music.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).music();
            name = "music";
            Prefs.setCategory_id(context, "7");
            Prefs.setChildID(context, child_id);
        });

        binding.spatial.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).spatial();
            name = "spatial";
            Prefs.setCategory_id(context, "6");
            Prefs.setChildID(context, child_id);
        });

        binding.interpersonal.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).interpersonal();
            name = "interpersonal";
            Prefs.setCategory_id(context, "5");
            Prefs.setChildID(context, child_id);
        });

        binding.intrapersonal.setOnClickListener(view1 -> {
            ((InsideExplorer) getActivity()).intrapersonal();
            Prefs.setCategory_id(context, "4");
            Prefs.setChildID(context, child_id);
            name = "intrapersonal";
        });

        try {
            childs.clear();
            childDetails1.clear();
            JSONArray jsonArray = new JSONArray(Prefs.getChildDetails(context));
            //Log.d("Arraym", String.valueOf(jsonArray.length()));
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

        }


    }

    public interface InsideExplorer {
        void environment();

        void interpersonal();

        void intrapersonal();

        void language();

        void music();

        void physical();

        void logic();

        void spatial();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}