package com.revolution.stepup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.text.InputType;
import android.content.DialogInterface;

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

import static android.app.Activity.RESULT_OK;

/**
 * Created by Nandash on 05-02-2017.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<People> mDataSet;

    public EventAdapter(Context context, ArrayList<People> people) {
        this.context = context;
        this.mDataSet = people;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView uName;
        public Button thanksButton;

        public ViewHolder(View itemView) {
            super(itemView);
            uName = (TextView) itemView.findViewById(R.id.eventUserName);
            thanksButton = (Button) itemView.findViewById(R.id.sayThanks);
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
                .inflate(R.layout.people_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.uName.setText(mDataSet.get(position).uName);
        //((ViewHolder) holder).textView2.setText(mDataSet.get(position).imageUrl);
        final Button fb = viewHolder.thanksButton;

        ((ViewHolder) holder).thanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Enter a Message");
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Post tp persons profile here
                        RequestQueue queue = Volley.newRequestQueue(context);
                        StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/sendMessage", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

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
                                params.put("uid_2",mDataSet.get(position).uid);
                                params.put("content",input.getText().toString());
                                java.util.Date date = Calendar.getInstance().getTime();
                                params.put("date", DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString());
                                params.put("image_url","");
                                //params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                return params;
                            }
                        };
                        queue.add(sr);
                        //Toast.makeText(context, input.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        } );
    }

}

