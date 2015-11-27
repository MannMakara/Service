package com.hammersmith.john.service.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class Tab3 extends Fragment {

    ListView listPlace;
    TextView textView;

    CustomAdapterPlace adapterFavorPlace;
    List<Place> placeList = new ArrayList<Place>();
    Place place;

    String codeID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_3, container, false);
        listPlace = (ListView) v.findViewById(R.id.favor_list);

        textView = (TextView) v.findViewById(R.id.errorText);

        adapterFavorPlace = new CustomAdapterPlace(getActivity(), placeList);
        listPlace.setAdapter(adapterFavorPlace);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        codeID = ((TestTabActivity) getActivity()).getLastCode();

        /*JSON Request */
        if (placeList.size() <= 0) {
            JsonArrayRequest favorReq = new JsonArrayRequest(Constant.URL_LIST_FAVOR_PLACE + codeID, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(i);
                            place = new Place();
                            place.setName(object.getString("customer_name"));
                            place.setAddress(object.getString("address"));
                            place.setImage(Constant.URL_HOME + object.getString("logo_path"));
                            placeList.add(place);
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

        Toast.makeText(getActivity(), codeID, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        preferences.edit().remove("CodeID").commit();
    }
}
