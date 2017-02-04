package com.revolution.stepup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

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
        public Button followButton;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.buddyUserName);
            followButton = (Button) itemView.findViewById(R.id.buddyFollowButton);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).textView1.setText(mDataSet.get(position).username);
        //((ViewHolder) holder).textView2.setText(mDataSet.get(position).imageUrl);
        final Button fb = ((ViewHolder) holder).followButton;
        if(mDataSet.get(position).followStatus) {
            ((ViewHolder) holder).followButton.setText("Unfollow");
            ((ViewHolder) holder).followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestQueue queue = Volley.newRequestQueue(context);
                    StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/unfollow", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            fb.setText("Follow");
                            mDataSet.get(position).followStatus = false;
                            notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error!", error.toString());
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("uid_1", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            params.put("uid_2", mDataSet.get(position).uid);
                            return params;
                        }
                    };
                    queue.add(sr);
                }
            });
        }else {
            ((ViewHolder) holder).followButton.setText("Follow");
            ((ViewHolder) holder).followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestQueue queue = Volley.newRequestQueue(context);
                    StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/follow", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                            fb.setText("Unfollow");
                            mDataSet.get(position).followStatus = true;
                            notifyDataSetChanged();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley Error!", error.toString());
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("uid_1", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            params.put("uid_2", mDataSet.get(position).uid);
                            return params;
                        }
                    };
                    queue.add(sr);
                }
            });
        }
        //((ViewHolder) holder).textView3.setText(mDataSet.get(position).email);


    }
}
