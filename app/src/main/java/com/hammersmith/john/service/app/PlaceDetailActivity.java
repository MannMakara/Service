package com.hammersmith.john.service.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.adapter.PlaceRecyclerAdapter;
import com.hammersmith.john.service.adapter.PlaceViewPager;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import utils.Constant;

public class PlaceDetailActivity extends AppCompatActivity {

    // Need this to link with the Snackbar
    private CoordinatorLayout mCoordinator;
    //Need this to set the title of the app bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPager mPager;
    private TabLayout mTabLayout;

    String detail_place_title;

    public static int placeID;

    ImageView img;

    /*New Adapter*/

    private PlaceViewPager mAdapter;
    public static Place place;


    FloatingActionButton fab;

    private boolean mSingInClicked = false;
    private String mGoogleCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);

        img = (ImageView) findViewById(R.id.img_place);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(mToolbar);

//        img.setImageResource(R.drawable.sokha); // can add Image here

        mAdapter = new PlaceViewPager(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setAdapter(mAdapter);

        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        detail_place_title = getIntent().getStringExtra("title");
        placeID = Integer.parseInt(getIntent().getStringExtra("id"));

        mCollapsingToolbarLayout.setTitle(detail_place_title);

        //Floating Action Button

            mSingInClicked = Tab4.mSignInClicked;
            mGoogleCode = Tab4.mGoogleCode;

            if (mSingInClicked){
                StringRequest request = new StringRequest(Request.Method.GET,Constant.URL_DETECT_FAVOR_PLACE + mGoogleCode + "/" + placeID, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        VolleyLog.d("String : %s",s);
                        if ("\uFEFFsuccess".equals(s)){
                            fab.setImageResource(R.drawable.ic_favorite_white_48dp);
                        }
                        else if ("\uFEFFalready_have".equals(s)){
                            fab.setImageResource(R.drawable.heart);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(getApplicationContext(),volleyError+"",Toast.LENGTH_LONG).show();
                    }
                });
                AppController.getInstance().addToRequestQueue(request);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StringRequest requestPost = new StringRequest(Request.Method.GET,Constant.URL_ADD_FAVOR_PLACE + mGoogleCode + "/" + placeID, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            VolleyLog.d("String : %s",s);
                            if ("\uFEFFsuccess".equals(s)){
                                fab.setImageResource(R.drawable.heart);
                                Snackbar snackbar = Snackbar.make(mCoordinator,"Added to list favorite",Snackbar.LENGTH_INDEFINITE);
                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                            else if ("\uFEFFalready_have".equals(s)){
                                fab.setImageResource(R.drawable.heart);
                                Snackbar snackbar = Snackbar.make(mCoordinator,"It's already in your favorite box.",Snackbar.LENGTH_INDEFINITE);
                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(getApplicationContext(),volleyError+"",Toast.LENGTH_LONG).show();
                        }
                    });
                    AppController.getInstance().addToRequestQueue(requestPost);
                }
            });

            fab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Toast.makeText(getApplicationContext(), "Long Click", Toast.LENGTH_SHORT).show();
                    StringRequest deleteRequest = new StringRequest(Request.Method.GET, Constant.URL_DELETE_FAVOR_PLACE + mGoogleCode + "/" + placeID, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            VolleyLog.d("String : %s",s);
                            if ("\uFEFFsuccess".equals(s)){
                                fab.setImageResource(R.drawable.ic_favorite_white_48dp);
                                Snackbar snackbar = Snackbar.make(mCoordinator,"Your Favorite was deleted!",Snackbar.LENGTH_INDEFINITE);
                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                            else if ("\uFEFFnot_favorite_yet".equals(s)){
                                fab.setImageResource(R.drawable.ic_favorite_white_48dp);
//                                Snackbar snackbar = Snackbar.make(mCoordinator,"It's already in your favorite box.",Snackbar.LENGTH_INDEFINITE);
//                                // Changing action button text color
//                                View sbView = snackbar.getView();
//                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//                                textView.setTextColor(Color.YELLOW);
//                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                    AppController.getInstance().addToRequestQueue(deleteRequest);
                    return true;
                }
            });
        }
        else {
                fab.setImageResource(R.drawable.ic_favorite_white_48dp);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar = Snackbar.make(mCoordinator,"Please Log In",Snackbar.LENGTH_INDEFINITE);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
        }


    }

    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";

        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            int id_detail = PlaceDetailActivity.placeID;
            place = new Place();

//            Bundle arguments = getArguments();
            RecyclerView recyclerView = new RecyclerView(getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new PlaceRecyclerAdapter(getActivity()));
            return recyclerView;
        }
    }

}
