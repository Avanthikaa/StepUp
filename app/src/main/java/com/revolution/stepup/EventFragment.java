package com.revolution.stepup;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ImageView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    private OnFragmentInteractionListener mListener;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_layout, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        //searchName = (SearchView) rootView.findViewById(R.id.searchName);
        //searchName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //@Override
            //public boolean onQueryTextSubmit(String query) {
              //  Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                loadData();
                //return true;
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        data = new ArrayList<People>();
        eventAdapter = new EventAdapter(rootView.getContext(), data);
        recyclerView.setAdapter(eventAdapter);
             return rootView;
    }

    void loadData() {

        data.add(new People("Varun", "img"));
        data.add(new People("Ashwini", "img"));
        data.add(new People("Avan", "img"));
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.GET, GoogleSignInActivity.SERVER_URL + "/search", new Response.Listener<String>() {
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
                    Glide.with(getContext()).load("https://www.google.co.in/search?q=sunset&source=lnms&tbm=isch&sa=X&ved=0ahUKEwitxZndyPfRAhXMsI8KHWuFBAAQ_AUICCgB&biw=1366&bih=638#imgrc=pajtY2QE8FLdnM:").into(imageView);
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                    eventAdapter.notifyDataSetChanged();

                }catch (Exception ex){
                    Log.e("EventFragment",ex.toString());
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
                params.put("eid", "4");
                //params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                return params;
            }
        };
        queue.add(sr);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
