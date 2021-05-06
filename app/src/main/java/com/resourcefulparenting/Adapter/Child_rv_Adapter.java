package com.resourcefulparenting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.resourcefulparenting.R;
import com.resourcefulparenting.fragment.ActivityListingFragment;
import com.resourcefulparenting.fragment.MyAccountGeneralFragment;
import com.resourcefulparenting.models.ActivityComListingResponse;
import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.models.Login.LoginResponse;

import java.util.ArrayList;
import java.util.List;

public class Child_rv_Adapter extends RecyclerView.Adapter<Child_rv_Adapter.ViewHolder> {


    Context context;
    ArrayList<ChildDetails> childDetails;


    public Child_rv_Adapter(ArrayList<ChildDetails> details, Context context) {
        this.childDetails = details;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.child_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //nana changes
        final  ChildDetails  items=childDetails.get(position);
        holder.child_name.setText(items.getChild_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MyAccountGeneralFragment.InsideAccount)context).changeProfile(items.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return childDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView child_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            child_name = itemView.findViewById(R.id.child_name_row_text);
        }
    }
}
