package com.revolution.stepup;

/**
 * Created by vvvro on 2/5/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<ProfileMessage> mDataSet;

    public ProfileAdapter(Context context, ArrayList<ProfileMessage> events) {
        this.context = context;
        this.mDataSet = events;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView profileUserName;
        public TextView profileUserMessage;
        public ImageView profileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            profileUserName = (TextView) itemView.findViewById(R.id.profileUserName);
            profileUserMessage = (TextView) itemView.findViewById(R.id.profileMessage);
            profileImage = (ImageView) itemView.findViewById(R.id.profileMessageImage);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.profile_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).profileUserName.setText(mDataSet.get(position).userName);
        ((ViewHolder) holder).profileUserMessage.setText(mDataSet.get(position).message);
        Glide.with(context).load(mDataSet.get(position).imageURL).into(((ViewHolder) holder).profileImage);
    }
}