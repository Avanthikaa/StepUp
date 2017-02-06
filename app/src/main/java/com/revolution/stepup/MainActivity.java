package com.revolution.stepup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(FirebaseAuth.getInstance().getCurrentUser()==null) {
            Intent intent = new Intent(this, GoogleSignInActivity.class);
            startActivity(intent);
        }
       /* TextView textView = (TextView) findViewById(R.id.fuid);
        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getUid());*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadDataFragment uploadDataFragment = new UploadDataFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, uploadDataFragment).commit();
            }
        });
        //Hello
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        try {
            TextView textView = (TextView) headerView.findViewById(R.id.mainUserName);
            textView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            TextView textView1 = (TextView) headerView.findViewById(R.id.mainUserEmail);
            textView1.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            CircleImageView imageView = (CircleImageView) headerView.findViewById(R.id.imageView);
            Glide.with(getApplicationContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imageView);
        }catch (Exception ex){
            Log.e("MainActivity",ex.toString());
        }
        navigationView.setNavigationItemSelectedListener(this);

        NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newsFeedFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter a Message");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Post tp persons profile here
                    if(input.getText().toString().equals("")){
                        Snackbar.make(MainActivity.this.getCurrentFocus(), "Please enter a phone number", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return;
                    }else if(input.getText().toString().length()<10){
                        Snackbar.make(MainActivity.this.getCurrentFocus(), "Please enter a valid phone number", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else{
                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                        StringRequest sr = new StringRequest(Request.Method.POST,GoogleSignInActivity.SERVER_URL+"/setPhoneNumber", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.v("MainActivity",response);
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
                                params.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                params.put("phone", input.getText().toString());
                                return params;
                            }
                        };
                        queue.add(sr);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, newsFeedFragment).commit();
        } else if (id == R.id.nav_gallery) {
            BuddyFragment buddyFragment=new BuddyFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,buddyFragment).commit();

        } else if (id == R.id.nav_slideshow) {
            CreateEventFragment createEventFragment = new CreateEventFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, createEventFragment).commit();
        } else if (id == R.id.nav_manage) {
            MyEventsFragment myEventsFragment = new MyEventsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, myEventsFragment).commit();
        } else if (id == R.id.nav_share) {
            ProfileFragment profileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, profileFragment).commit();
        } else if (id == R.id.nav_send) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, GoogleSignInActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
