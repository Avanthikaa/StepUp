package com.revolution.stepup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class CreateEventFragment extends Fragment {

    final Calendar calendar = Calendar.getInstance();
    ImageView imageView;
    String base64 = "";
    EditText eventDateTextInputLayout;

    private Uri imageURI = null;
    private InputStream inputStream = null;

    public ProgressBar progressBar;

    String eventName, eventDescription, eventAddress;
    public CreateEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_create_event, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.eventCreatorImage);
        progressBar = (ProgressBar) rootView.findViewById(R.id.createEventProgress);
        progressBar.setVisibility(View.INVISIBLE);
        Glide.with(getContext()).load(R.drawable.sunset1).into(imageView);
        inputStream = null;
        imageURI = null;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 6969);
            }
        });
        base64 = "";
        Button button = (Button) rootView.findViewById(R.id.createEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout eventNameTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.createEventName);
                TextInputLayout eventDescriptionTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.createEventDescription);
                TextInputLayout eventAddressTextInputLayout = (TextInputLayout) rootView.findViewById(R.id.createEventAddress);
                if (eventNameTextInputLayout.getEditText().getText().toString().matches("") || eventDescriptionTextInputLayout.getEditText().getText().toString().matches("") || eventAddressTextInputLayout.getEditText().getText().toString().matches("")) {
                    Snackbar.make(view, "Please enter data in all the fields", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Editable editable1 = eventNameTextInputLayout.getEditText().getText();
                Editable editable2 = eventDescriptionTextInputLayout.getEditText().getText();
                Editable editable3 = eventAddressTextInputLayout.getEditText().getText();
                eventName = editable1.toString();
                eventDescription = editable2.toString();
                eventAddress = editable3.toString();
                if (eventDateTextInputLayout.getText().toString().matches("")) {
                    Snackbar.make(view, "Please choose a Date", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                /*if (base64.matches("")) {
                    Snackbar.make(view, "Please pick an Image", Snackbar.LENGTH_SHORT).show();
                    return;
                }*/
                long currTime = calendar.getTime().getTime();
                if(inputStream==null) {
                    Snackbar.make(view, "Please choose an Image", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                if(eventDateTextInputLayout.getText().toString().equals("")){
                    Snackbar.make(view, "Please choose an Image", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
                downloadFilesTask.execute(inputStream);
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };
        eventDateTextInputLayout = (EditText) rootView.findViewById(R.id.createEventDate);
        eventDateTextInputLayout.setInputType(InputType.TYPE_NULL);
        eventDateTextInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return rootView;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.US);
        eventDateTextInputLayout.setText(simpleDateFormat.format(calendar.getTime()));
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
            Toast.makeText(getContext(), "Post Execute", Toast.LENGTH_SHORT).show();
            final String imageURL = s;
            //Toast.makeText(getContext(), "Data to upload "+eventName+" "+eventDescription+" "+eventAddress+" "+s+" "+eventDateTextInputLayout.getText().toString(), Toast.LENGTH_SHORT).show();
            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL+"/createEvent", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                    /*imageDescription.setText("");
                    FragmentManager fragmentManager = getFragmentManager();
                    InspireFragment inspireFragment = new InspireFragment();
                    fragmentManager.beginTransaction().replace(R.id.content_main, inspireFragment).commit();*/
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
                    getFragmentManager().beginTransaction().replace(R.id.content_main, newsFeedFragment).commit();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley Error!",error.toString());
                }
            }){

                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    params.put("description",eventDescription);
                    params.put("image_url", imageURL);
                    params.put("event_name",eventName);
                    params.put("event_date",eventDateTextInputLayout.getText().toString()+" 00:00:00");
                    java.util.Date date = Calendar.getInstance().getTime();
                    params.put("created_date", DateFormat.format("yyyy-MM-dd HH:mm:ss",date).toString());
                    params.put("venue", eventAddress);
                    return params;
                }
            };
            queue.add(sr);
        }
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
                    imageURI = data.getData();
                    Glide.with(this).load(data.getData()).into(imageView);
                }catch (Exception ex){
                    Log.v("MainActivity","Image not Found!");
                }
            }
        }
    }
}
