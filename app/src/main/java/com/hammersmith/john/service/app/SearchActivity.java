package com.hammersmith.john.service.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.adapter.CustomAdapterPlace;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.Constant;

public class SearchActivity extends AppCompatActivity {

    String actionBarName;
    ListView listView;
    CustomAdapterPlace adapterPlace;
    List<Place> placeList = new ArrayList<Place>();
    Place place;
    String[] place_id;
    String[] place_title;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        actionBarName = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle("Search: " + actionBarName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (TextView) findViewById(R.id.errorText);
        listView = (ListView) findViewById(R.id.search_list);
        adapterPlace = new CustomAdapterPlace(this,placeList);
        listView.setAdapter(adapterPlace);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                Intent intent = new Intent(getApplicationContext(), PlaceDetailActivity.class);
                intent.putExtra("id", place_id[position]);
                intent.putExtra("title", place_title[position]);
                startActivity(intent);
            }
        });

        /*JsonArrayRequest for Search*/
        if (placeList.size() <=0) {
            JsonArrayRequest searchReq = new JsonArrayRequest(Constant.URL_SEARCH + actionBarName, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    place_id = new String[jsonArray.length()];
                    place_title = new String[jsonArray.length()];
                    if (jsonArray.length() == 0){
                        textView.setVisibility(View.VISIBLE);
                    }
                    for (int i=0 ; i < jsonArray.length(); i++){
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            place = new Place();
                            place.setName(obj.getString("customer_name"));
                            place.setAddress(obj.getString("address"));
                            place.setImage(Constant.URL_INDEX + obj.getString("logo_path"));
                            place_id[i] = obj.getString("id");
                            place_title[i] = obj.getString("customer_name");
                            placeList.add(place);
                        } catch (JSONException e) {
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
            AppController.getInstance().addToRequestQueue(searchReq);
        }
        /*JsonArrayRequest for Search*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
