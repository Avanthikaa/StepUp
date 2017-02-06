package com.revolution.stepup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;



public class EventFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<People> data = null;
    EventAdapter eventAdapter;
    ImageView imageView;

    InputStream inputStream = null;
    URI imageURI = null;
    String textInput = "";

    SwipeRefreshLayout eventRefresh;

    ProgressBar progressBar;

    Button broadCastButton;

    String eid;


    public EventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventFragment newInstance(String param1, String param2) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_layout, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.peopleRecyclerView);
        eid = getArguments().getString("eid");
        //searchName = (SearchView) rootView.findViewById(R.id.searchName);
        //searchName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        //@Override
        //public boolean onQueryTextSubmit(String query) {
        //  Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();

        //return true;
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        data = new ArrayList<People>();
        imageView = (ImageView) rootView.findViewById(R.id.eventImage);

        progressBar = (ProgressBar) rootView.findViewById(R.id.eventProgressBar);

        eventRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.eventRefresh);
        eventRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventRefresh.setRefreshing(false);
                loadData();
            }
        });



        broadCastButton = (Button) rootView.findViewById(R.id.broadcastButton);
        broadCastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter a Message");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Post tp persons profile here
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/broadcast", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //Toast.makeText(getContext(),"Posted!",Toast.LENGTH_LONG).show();
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
                                params.put("eid", eid);
                                params.put("content",input.getText().toString());
                                java.util.Date date = Calendar.getInstance().getTime();
                                params.put("date", DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString());
                                params.put("image_url","");
                                //params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                return params;
                            }
                        };
                        queue.add(sr);
                    }
                });
                builder.setNegativeButton("Add Image", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(input.getEditableText().toString().equals("")){
                           textInput = "";
                        }else {
                            textInput = input.getEditableText().toString();
                        }
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 6969);
                    }
                });
                /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });*/
                builder.create().show();
                //builder.show();
            }
        });
        eventAdapter = new EventAdapter(rootView.getContext(), data);
        loadData();
        recyclerView.setAdapter(eventAdapter);
        return rootView;
    }

    void loadData() {

        /*data.add(new People("Varun", "img"));
        data.add(new People("Ashwini", "img"));
        data.add(new People("Avan", "img"));*/
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/getEvent", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    /*data.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("users");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        //JSONObject jsonObject2 = jsonObject1.getJSONObject("problem");
                        People post = new People(jsonObject1.getString("uid"),jsonObject1.getString("name"));
                        data.add(post);*/
                    /*Glide.with(getContext()).load("https://www.google.co.in/search?q=sunset&source=lnms&tbm=isch&sa=X&ved=0ahUKEwitxZndyPfRAhXMsI8KHWuFBAAQ_AUICCgB&biw=1366&bih=638#imgrc=pajtY2QE8FLdnM:").into(imageView);
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                    eventAdapter.notifyDataSetChanged();*/
                    data.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("attendees");

                        Glide.with(getContext()).load(new JSONObject(jsonObject.getString("event_details")).getString("image_url")).into(imageView);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            data.add(new People(jsonObject1.getString("name"),jsonObject1.getString("uid"),jsonObject1.getString("profile_image_url")));
                        }
                        eventAdapter.notifyDataSetChanged();
                    }catch (Exception ex){
                        Log.v("EventFragment",ex.toString());
                    }
                    Log.v("EventFragment", response);

                } catch (Exception ex) {
                    Log.e("EventFragment", ex.toString());
                }

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
                params.put("eid", eid);
                //params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                return params;
            }
        };
        queue.add(sr);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6969 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if(data==null){
                return;
            }else{
                try {
                    inputStream = getContext().getContentResolver().openInputStream(data.getData());
                    uploadAndSendToServer(inputStream, textInput);
                    //imageURI = data.getData();
                    //Glide.with(this).load(data.getData()).into(imageView);
                }catch (Exception ex){
                    Log.v("MainActivity","Image not Found!");
                }
            }
        }
    }

    public void uploadAndSendToServer(InputStream inputStream, String textInput){
        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute(inputStream);
    }

    private class DownloadFilesTask extends AsyncTask<InputStream, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(InputStream... params) {
            Map config = new HashMap();
            config.put("cloud_name", "dhjgpncfa");
            config.put("api_key", "834952152399412");
            config.put("api_secret", "tMDWR-9vYdmBIvu6d3SFfgnyX9c");
            final Cloudinary cloudinary = new Cloudinary(config);
            try {
                Map uploadResult = cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap());
                String url = (String) uploadResult.get("url");
                return url;
            }catch (Exception ex){
                Log.e("Image Upload Error", ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            //Upload to Server
            final String imageURL = s;
            //Toast.makeText(getContext(), "Data to upload "+eventName+" "+eventDescription+" "+eventAddress+" "+s+" "+eventDateTextInputLayout.getText().toString(), Toast.LENGTH_SHORT).show();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/broadcast", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getContext(),"Posted!",Toast.LENGTH_LONG).show();
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
                    params.put("eid", eid);
                    if(textInput==null||textInput.equals("")){
                        params.put("content","");
                    }else {
                        params.put("content", textInput);
                    }
                    java.util.Date date = Calendar.getInstance().getTime();
                    params.put("date", DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString());
                    if(imageURL==null||imageURL.equals("")) {
                        params.put("image_url", "");
                    }else{
                        params.put("image_url", imageURL.toString());
                    }
                    //params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    return params;
                }
            };
            queue.add(sr);
        }
    }

}
