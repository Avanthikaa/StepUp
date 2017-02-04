package com.revolution.stepup;

import android.content.Context;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BuddyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BuddyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuddyFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Buddy> data = null;
    BuddyAdapter buddyAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BuddyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuddyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuddyFragment newInstance(String param1, String param2) {
        BuddyFragment fragment = new BuddyFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_buddy, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        data = new ArrayList<Buddy>();
        buddyAdapter = new BuddyAdapter(rootView.getContext(),data);
        recyclerView.setAdapter(buddyAdapter);
        loadData();
        return rootView;
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

    void loadData(){
        data.add(new Buddy("Varun","img",true));
        data.add(new Buddy("Nethra","newurl",true));
        data.add(new Buddy("Ashwini","newurl",false));
        data.add(new Buddy("Avan","newurl",false));
        /*RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.GET,"http://codeforces.com/api/user.status?handle=Vrock", new Response.Listener<String>() {

            public void onResponse(String response) {
                //Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                data.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        //JSONObject jsonObject2 = jsonObject1.getJSONObject("problem");
                       // Buddy post = new Buddy(jsonObject2.getString("name"),jsonObject2.getString("type"));
                        data.add(post);
                    }
                    Toast.makeText(getContext(),"Done",Toast.LENGTH_LONG).show();

                    //postAdapter.notifyDataSetChanged();
                }catch (Exception ex){
                    Toast.makeText(getContext(),ex.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error!",error.toString());
            }
        }){
            /*
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
                params.put("from_user", sharedPreferences.getString("user_name",""));
                return params;
            }*/
            /*
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };*/
        //queue.add(sr);
    }
}