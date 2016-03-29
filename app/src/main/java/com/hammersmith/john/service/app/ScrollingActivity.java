package com.hammersmith.john.service.app;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Place;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Constant;

public class ScrollingActivity extends AppCompatActivity implements OnMapReadyCallback {

    String detail_place_title;

    public static int placeID;
    public static Place place;
    private boolean mSingInClicked = false;
    private String mGoogleCode = null;
    FloatingActionButton fab;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    String imageURL;

    TextView txtDetail,txtPhone,txtWeb,txtMail,txtPhone2;
    ImageView imageView;
    MapView mapView;
    GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        txtDetail = (TextView) findViewById(R.id.detail);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtPhone2 = (TextView) findViewById(R.id.txtPhone2);
        txtMail = (TextView) findViewById(R.id.txtMail);
        txtWeb = (TextView) findViewById(R.id.txtWeb);
        imageView = (ImageView) findViewById(R.id.backdrop);

        mapView = (MapView) findViewById(R.id.map);

        mapView.onCreate(null);
        mapView.getMapAsync(this);

        detail_place_title = getIntent().getStringExtra("title");
        placeID = Integer.parseInt(getIntent().getStringExtra("id"));
        
        collapsingToolbar.setTitle(detail_place_title);

        mSingInClicked = Tab4.mSignInClicked;
        mGoogleCode = Tab4.mGoogleCode;
        
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (mSingInClicked){
            StringRequest request = new StringRequest(Request.Method.GET, Constant.URL_DETECT_FAVOR_PLACE + mGoogleCode + "/" + placeID, new Response.Listener<String>() {
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
                }
            });
            AppController.getInstance().addToRequestQueue(request);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    StringRequest requestPost = new StringRequest(Request.Method.GET,Constant.URL_ADD_FAVOR_PLACE + mGoogleCode + "/" + placeID, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            VolleyLog.d("String : %s",s);
                            if ("\uFEFFsuccess".equals(s)){
                                fab.setImageResource(R.drawable.heart);
                                Snackbar snackbar = Snackbar.make(v,"Added to list favorite",Snackbar.LENGTH_INDEFINITE);
                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                            else if ("\uFEFFalready_have".equals(s)){
                                fab.setImageResource(R.drawable.heart);
                                Snackbar snackbar = Snackbar.make(v,"It's already in your favorite box.",Snackbar.LENGTH_INDEFINITE);
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
                        }
                    });
                    AppController.getInstance().addToRequestQueue(requestPost);
                }
            });

            fab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    StringRequest deleteRequest = new StringRequest(Request.Method.GET, Constant.URL_DELETE_FAVOR_PLACE + mGoogleCode + "/" + placeID, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            VolleyLog.d("String : %s",s);
                            if ("\uFEFFsuccess".equals(s)){
                                fab.setImageResource(R.drawable.ic_favorite_white_48dp);
                                Snackbar snackbar = Snackbar.make(v,"Your Favorite was deleted!",Snackbar.LENGTH_INDEFINITE);
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
        } else {
            fab.setImageResource(R.drawable.ic_favorite_white_48dp);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar snackbar = Snackbar.make(v,"Please Log In",Snackbar.LENGTH_INDEFINITE);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            });
        }
        /*JsonObjectRequest*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_PLACE + placeID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    txtDetail.setText(Html.fromHtml(jsonObject.getString("customer_desc")));
                    txtMail.setText(jsonObject.getString("email"));
                    txtPhone.setText(jsonObject.getString("work_number"));
                    txtPhone2.setText(jsonObject.getString("mobile_number"));
                    txtWeb.setText(jsonObject.getString("website"));
                    imageURL = Constant.URL_INDEX+jsonObject.getString("image");
//                    if (imageLoader == null)
//                        imageLoader = AppController.getInstance().getImageLoader();
//                    imageView.setImageUrl(imageURL,imageLoader);
                    Uri uri = Uri.parse(imageURL);
                    Picasso.with(getApplicationContext()).load(uri).into(imageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
            /*JsonObjectRequest*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        MapsInitializer.initialize(getApplicationContext());
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(11.570937,104.937177)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(11.570937,104.937177),16f);
        mGoogleMap.moveCamera(cameraUpdate);
    }
}
