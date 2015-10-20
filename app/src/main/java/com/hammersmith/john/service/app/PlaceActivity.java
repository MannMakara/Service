package com.hammersmith.john.service.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.adapter.CustomAdapterPlace;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.City;
import com.hammersmith.john.service.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.Constant;

public class PlaceActivity extends AppCompatActivity {

    CustomAdapterPlace adapterPlace;
    List<Place> placeList = new ArrayList<Place>();
    Place place;
    ListView listView;

    String[] place_id;
    String[] place_title;
    String actionBarName,place_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        actionBarName = getIntent().getStringExtra("title");
        place_url = getIntent().getStringExtra("url");

        getSupportActionBar().setTitle(actionBarName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list_place);

        adapterPlace = new CustomAdapterPlace(this,placeList);

        listView.setAdapter(adapterPlace);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                Intent intent = new Intent(getApplicationContext(),PlaceDetailActivity.class);
                intent.putExtra("id",place_id[position]);
                intent.putExtra("title",place_title[position]);
                startActivity(intent);
            }
        });

        if (placeList.size() <= 0){

            JsonArrayRequest placeReq = new JsonArrayRequest(Constant.URL_PLACE+place_url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    place_id = new String[jsonArray.length()];
                    place_title = new String[jsonArray.length()];
//                    Toast.makeText(getApplicationContext(),"Json Request "+jsonArray.length(),Toast.LENGTH_SHORT).show();
                    for (int i=0 ; i < jsonArray.length() ; i++){
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            place = new Place();
                            place.setName(object.getString("customer_name"));
                            place.setAddress(object.getString("address"));
                            place.setImage(Constant.URL_HOME + object.getString("logo_path"));
                            place_id[i] = object.getString("id");
                            place_title[i] = object.getString("customer_name");
                            placeList.add(place);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    adapterPlace.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), volleyError + "", Toast.LENGTH_SHORT).show();
                }
            });
            AppController.getInstance().addToRequestQueue(placeReq);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
