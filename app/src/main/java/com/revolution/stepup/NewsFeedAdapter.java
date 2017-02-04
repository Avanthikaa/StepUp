package com.revolution.stepup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by vvvro on 2/4/2017.
 */
public class NewsFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Post> mDataSet;
    private Context mContext;
    private RequestQueue queue;

    NewsFeedAdapter(Context context, ArrayList<Post> myDataSet) {
        mContext = context;
        mDataSet = myDataSet;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_text, parent, false);
            return new PostTextViewHolder(v);
        } else if (viewType == 2) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_image, parent, false);
            return new PostImageViewHolder(v);
        } else if (viewType == 3) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_image_text, parent, false);
            return new PostImageTextViewHolder(v);
        } else if(viewType == 4) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_event, parent, false);
            return new EventViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder.getItemViewType() == 1){
            PostTextViewHolder postTextViewHolder = (PostTextViewHolder) holder;
            postTextViewHolder.postUserName.setText(mDataSet.get(position).userName);
            postTextViewHolder.postUserDescription.setText(mDataSet.get(position).content);
            postTextViewHolder.postUserDate.setText(mDataSet.get(position).postedDate);
            Glide.with(mContext).load(mDataSet.get(position).userProfileURL).into(postTextViewHolder.postUserImage);
        }else if(holder.getItemViewType() == 2){
            PostImageViewHolder postImageViewHolder = (PostImageViewHolder) holder;
            postImageViewHolder.postImageUserName.setText(mDataSet.get(position).userName);
            Glide.with(mContext).load(mDataSet.get(position).userProfileURL).into(postImageViewHolder.postImageUserImage);
            Glide.with(mContext).load(mDataSet.get(position).imageURL).into(postImageViewHolder.postImageDescription);
            postImageViewHolder.postImageUserDate.setText(mDataSet.get(position).postedDate);
        }else if(holder.getItemViewType() == 3){
            PostImageTextViewHolder postImageTextViewHolder = (PostImageTextViewHolder) holder;
            postImageTextViewHolder.postImageTextUserName.setText(mDataSet.get(position).userName);
            postImageTextViewHolder.postImageTextDescription.setText(mDataSet.get(position).content);
            Glide.with(mContext).load(mDataSet.get(position).userProfileURL).into(postImageTextViewHolder.postImageTextUserImage);
            Glide.with(mContext).load(mDataSet.get(position).imageURL).into(postImageTextViewHolder.postImageTextImage);
            postImageTextViewHolder.postImageTextDate.setText(mDataSet.get(position).postedDate);
        }else if(holder.getItemViewType() == 4){
            final EventViewHolder eventViewHolder = (EventViewHolder) holder;
            eventViewHolder.eventTitle.setText(mDataSet.get(position).eventName);
            eventViewHolder.eventVenue.setText(mDataSet.get(position).eventVenue);
            eventViewHolder.eventDescription.setText(mDataSet.get(position).content);
            Glide.with(mContext).load(mDataSet.get(position).userProfileURL).into(eventViewHolder.eventUserImage);
            Glide.with(mContext).load(mDataSet.get(position).imageURL).into(eventViewHolder.eventImage);
            eventViewHolder.eventPostedDate.setText(mDataSet.get(position).postedDate);
            eventViewHolder.eventDate.setText(mDataSet.get(position).eventDate);
            eventViewHolder.eventUserName.setText(mDataSet.get(position).userName);
            if(mDataSet.get(position).attending) {
                eventViewHolder.attendButton.setText("Miss");
                final Button button = eventViewHolder.attendButton;
                eventViewHolder.attendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RequestQueue queue = Volley.newRequestQueue(mContext);
                        StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/notGoingToEvent", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                mDataSet.get(position).attending = false;
                                button.setText("Attend");
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
                                params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                params.put("eid", mDataSet.get(position).eid);
                                return params;
                            }
                        };
                        queue.add(sr);
                    }
                });
            }else{
                eventViewHolder.attendButton.setText("Attend");
                final Button button = eventViewHolder.attendButton;
                eventViewHolder.attendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Attend
                        RequestQueue queue = Volley.newRequestQueue(mContext);
                        StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/goingToEvent", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                mDataSet.get(position).attending = true;
                                button.setText("Skip");
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
                                params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                params.put("eid", mDataSet.get(position).eid);
                                return params;
                            }
                        };
                        queue.add(sr);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).type;
    }

    class PostTextViewHolder extends RecyclerView.ViewHolder {
        TextView postUserName;
        TextView postUserDescription;
        TextView postUserDate;
        CircleImageView postUserImage;

        PostTextViewHolder(View v) {
            super(v);
            postUserName = (TextView) v.findViewById(R.id.postUserName);
            postUserDescription = (TextView) v.findViewById(R.id.postDescription);
            postUserDate = (TextView) v.findViewById(R.id.postDate);
            postUserImage = (CircleImageView) v.findViewById(R.id.postUserImage);
        }
    }

    class PostImageViewHolder extends RecyclerView.ViewHolder {
        TextView postImageUserName;
        ImageView postImageDescription;
        TextView postImageUserDate;
        CircleImageView postImageUserImage;

        PostImageViewHolder(View v) {
            super(v);
            postImageUserName = (TextView) v.findViewById(R.id.postImageUserName);
            postImageDescription = (ImageView) v.findViewById(R.id.postImageDescription);
            postImageUserDate = (TextView) v.findViewById(R.id.postImageDate);
            postImageUserImage = (CircleImageView) v.findViewById(R.id.postImageUserImage);
        }
    }

    class PostImageTextViewHolder extends RecyclerView.ViewHolder {
        TextView postImageTextUserName;
        TextView postImageTextDescription;
        TextView postImageTextDate;
        ImageView postImageTextImage;
        CircleImageView postImageTextUserImage;

        PostImageTextViewHolder(View v) {
            super(v);
            postImageTextUserName = (TextView) v.findViewById(R.id.postImageTextUserName);
            postImageTextDescription = (TextView) v.findViewById(R.id.postImageTextDescription);
            postImageTextDate = (TextView) v.findViewById(R.id.postImageTextDate);
            postImageTextImage = (ImageView) v.findViewById(R.id.postImageTextImage);
            postImageTextUserImage = (CircleImageView) v.findViewById(R.id.postImageTextUserImage);
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventUserName;
        TextView eventDescription;
        TextView eventTitle;
        TextView eventPostedDate;
        TextView eventDate;
        TextView eventVenue;
        ImageView eventImage;
        CircleImageView eventUserImage;
        Button attendButton;

        EventViewHolder(View v) {
            super(v);
            eventUserName = (TextView) v.findViewById(R.id.eventUserName);
            eventDescription = (TextView) v.findViewById(R.id.eventDescription);
            eventPostedDate = (TextView) v.findViewById(R.id.eventDate);
            eventDate = (TextView) v.findViewById(R.id.eventDateTime);
            eventVenue = (TextView) v.findViewById(R.id.eventLocation);
            eventImage = (ImageView) v.findViewById(R.id.eventImage);
            eventUserImage = (CircleImageView) v.findViewById(R.id.eventUserImage);
            eventTitle = (TextView) v.findViewById(R.id.eventTitle);
            attendButton = (Button) v.findViewById(R.id.attendButton);
        }
    }
}

