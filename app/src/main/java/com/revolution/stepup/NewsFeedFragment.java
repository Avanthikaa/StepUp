package com.revolution.stepup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditText postText;
    public Button postButton;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NewsFeedAdapter newsFeedAdapter;

    private SwipeRefreshLayout feedRefresh;

    private ArrayList<Post> data;

    private OnFragmentInteractionListener mListener;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        postText = (EditText) rootView.findViewById(R.id.postText);
        postButton = (Button) rootView.findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = postText.getEditableText().toString();
                if (text == null) {
                    Toast.makeText(getContext(), "Please enter something!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (text.equals("")) {
                    Toast.makeText(getContext(), "Please enter something!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (text.length() > 500) {
                    Toast.makeText(getContext(), "Post should not be more than 500 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestQueue queue = Volley.newRequestQueue(getContext());
                StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/createPost", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
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
                        params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                        java.util.Date date = Calendar.getInstance().getTime();
                        params.put("date", DateFormat.format("yyyy-MM-dd HH:mm:ss", date).toString());
                        params.put("content", text);
                        params.put("image_url", "null");
                        params.put("type", "1");
                        return params;
                    }
                };
                queue.add(sr);
                postText.setText("");
            }
        });
        feedRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.feedSwipeRefresh);
        feedRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                feedRefresh.setRefreshing(false);
            }
        });
        recyclerView = (RecyclerView) rootView.findViewById(R.id.feedRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        data = new ArrayList<Post>();
        loadData();
        newsFeedAdapter = new NewsFeedAdapter(getContext(), data);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsFeedAdapter);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void loadData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, GoogleSignInActivity.SERVER_URL + "/getNewsFeed", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    data.clear();
                    Log.v("NewsFeed",response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("posts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            int t = Integer.parseInt(jsonObject1.getString("type"));
                            switch (t) {
                                case 1:
                                    data.add(new Post(jsonObject1.getString("pid"), jsonObject1.getString("name"), jsonObject1.getString("uid"), jsonObject1.getString("date"), jsonObject1.getString("profile_image_url"), jsonObject1.getString("content")));
                                    break;
                                case 2:
                                    data.add(new Post(jsonObject1.getString("pid"), jsonObject1.getString("name"), jsonObject1.getString("uid"), jsonObject1.getString("date"), jsonObject1.getString("profile_image_url"), "", jsonObject1.getString("image_url")));
                                    break;
                                case 3:
                                    data.add(new Post(jsonObject1.getString("pid"), jsonObject1.getString("name"), jsonObject1.getString("uid"), jsonObject1.getString("date"), jsonObject1.getString("profile_image_url"), jsonObject1.getString("content"), jsonObject1.getString("image_url")));
                                    break;
                                case 4:
                                    JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("content"));
                                    data.add(new Post(jsonObject1.getString("pid"), jsonObject1.getString("name"), jsonObject1.getString("uid"), jsonObject1.getString("date"), jsonObject1.getString("profile_image_url"), jsonObject2.getString("description"), jsonObject1.getString("image_url"),jsonObject2.getString("event_date"),jsonObject2.getString("event_name"),jsonObject2.getString("venue"),jsonObject2.getBoolean("is_attending"),jsonObject2.getString("eid")));
                                    break;
                            }
                        }
                        newsFeedAdapter.notifyDataSetChanged();

                    } catch (Exception ex) {
                        Log.v("NewsFeedFragment", ex.toString());
                    }
                    //Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Log.e("BuddyFragment", ex.toString());
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
                params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                return params;
            }
        };
        queue.add(sr);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
