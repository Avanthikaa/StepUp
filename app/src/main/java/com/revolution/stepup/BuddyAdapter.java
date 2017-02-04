package com.revolution.stepup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by Nandash on 04-02-2017.
 */

public class BuddyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<Buddy> mDataSet;

    public BuddyAdapter(Context context, ArrayList<Buddy> buddies) {
        this.context = context;
        this.mDataSet = buddies;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.userName);
            textView2 = (TextView) itemView.findViewById(R.id.followStatus);
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
                .inflate(R.layout.layout_buddy, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).textView1.setText(mDataSet.get(position).username);
        //((ViewHolder) holder).textView2.setText(mDataSet.get(position).imageUrl);
        if(mDataSet.get(position).followStatus)
          ((ViewHolder) holder).textView2.setText("Following");
        else
            ((ViewHolder) holder).textView2.setText("");
        //((ViewHolder) holder).textView3.setText(mDataSet.get(position).email);


    }
}
