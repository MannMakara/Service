package com.hammersmith.john.service.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.adapter.CustomAdapterCity;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Category;
import com.hammersmith.john.service.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.Constant;

public class SpaActivity extends AppCompatActivity {
    ListView listView;
    CustomAdapterCity adapterCity;
    List<City> cityList = new ArrayList<City>();
    City city;

    int[] id;
    String[] title;
    String actionBarName;
    String cateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spa);

        actionBarName = getIntent().getStringExtra("title");
        cateID = getIntent().getStringExtra("id");
        getSupportActionBar().setTitle(actionBarName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(R.id.list_pro);

        adapterCity = new CustomAdapterCity(this,cityList);

        listView.setAdapter(adapterCity);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                Intent intent = new Intent(getApplicationContext(),PlaceActivity.class);
                intent.putExtra("url",cateID+"/"+id[position]);
                intent.putExtra("title",title[position]);
                startActivity(intent);
            }
        });

        if (cityList.size() <= 0){

            // Creating volley request obj
            JsonArrayRequest cityReq = new JsonArrayRequest(Constant.URL_CITY, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    id = new int[jsonArray.length()];
                    title = new String[jsonArray.length()];
                    for (int i=0; i< jsonArray.length() ; i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            city = new City();
                            city.setImage(obj.getString("image_city"));
                            city.setName(obj.getString("city_name"));
                            id[i] = obj.getInt("id");
                            title[i] = obj.getString("city_name");
                            cityList.add(city);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // notifying list adapter about data changes
                    // so that it renders the list view with updated data
                adapterCity.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), volleyError + "", Toast.LENGTH_SHORT).show();
                }
            });
            AppController.getInstance().addToRequestQueue(cityReq);

        }

    }

    @Override
    protected void onResume() {
        getSupportActionBar().setTitle(actionBarName);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setTitle(actionBarName);
        super.onBackPressed();
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
