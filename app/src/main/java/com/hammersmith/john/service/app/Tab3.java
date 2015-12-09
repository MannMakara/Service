package com.hammersmith.john.service.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import utils.Constant;

/**
 * Created by John on 8/24/2015.
 */
public class Tab3 extends Fragment{

    ListView listPlace;
    TextView textView;

    CustomAdapterPlace adapterFavorPlace;
    List<Place> placeList = new ArrayList<Place>();
    Place place;

    String[] placeID;
    String[] titile;

    String codeID = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_3, container, false);
        listPlace = (ListView) v.findViewById(R.id.favor_list);

        textView = (TextView) v.findViewById(R.id.errorText);
        codeID = ((TestTabActivity) getActivity()).getLastCode();
//        if (codeID == null){
//            Toast.makeText(getActivity(),"On Create View Null code id codition",Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(getActivity(),"On Create Not Null code id codition",Toast.LENGTH_LONG).show();
//        }


//        if (codeID == null){
//            textView.setVisibility(View.VISIBLE);
//            listPlace.setVisibility(View.GONE);
//            listPlace.setAdapter(null);
//        }
//        else {
//            textView.setVisibility(View.GONE);
//            listPlace.setVisibility(View.VISIBLE);
//        }
////        adapterFavorPlace = new CustomAdapterPlace(getActivity(), placeList);
////        listPlace.setAdapter(adapterFavorPlace);
////
////        adapterFavorPlace.notifyDataSetChanged();

        return v;
    }

//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        codeID = ((TestTabActivity) getActivity()).getLastCode();
//        if (codeID == null){
//            Toast.makeText(getActivity(),"On Restore Null code id codition",Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(getActivity(),"On Restore Not Null code id codition",Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        codeID = ((TestTabActivity) getActivity()).getLastCode();
//        if (codeID == null){
//            Toast.makeText(getActivity(),"On Create Null code id codition",Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(getActivity(),"On Create Not Null code id codition",Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        codeID = ((TestTabActivity) getActivity()).getLastCode();
//        if (codeID == null){
//            Toast.makeText(getActivity(),"On Start Null code id codition",Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(getActivity(),"On Start Not Null code id codition",Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();

        codeID = ((TestTabActivity) getActivity()).getLastCode();

//        Toast.makeText(getActivity(), codeID, Toast.LENGTH_SHORT).show();

        if (codeID == null){
//            Toast.makeText(getActivity(),"On Resume Null code id codition",Toast.LENGTH_LONG).show();
            textView.setVisibility(View.VISIBLE);
            listPlace.setVisibility(View.GONE);
            listPlace.setAdapter(null);
        }
        else {
//            Toast.makeText(getActivity(),"On Resume Not Null Condition",Toast.LENGTH_LONG).show();
            placeList.clear();
            textView.setVisibility(View.GONE);
            listPlace.setVisibility(View.VISIBLE);
            /*JSON Request */
            if (placeList.size() <= 0) {
                JsonArrayRequest favorReq = new JsonArrayRequest(Constant.URL_LIST_FAVOR_PLACE + codeID, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        placeID = new String[jsonArray.length()];
                        titile = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                place = new Place();
                                place.setName(object.getString("customer_name"));
                                place.setAddress(object.getString("address"));
                                place.setImage(Constant.URL_HOME + object.getString("logo_path"));
                                placeList.add(place);
                                placeID[i] = object.getString("place_id");
                                titile[i] = object.getString("customer_name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapterFavorPlace.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

                AppController.getInstance().addToRequestQueue(favorReq);
            }
        /*JSON Request */
            adapterFavorPlace = new CustomAdapterPlace(getActivity(), placeList);
            adapterFavorPlace.notifyDataSetChanged();
//            Toast.makeText(getActivity(), codeID, Toast.LENGTH_SHORT).show();

            listPlace.setAdapter(adapterFavorPlace);

            listPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
                    intent.putExtra("id", placeID[position]);
                    intent.putExtra("title", titile[position]);
                    startActivity(intent);
                }
            });

        }

    }

}
