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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.controller.AppController;

import org.json.JSONArray;

import utils.Constant;

/**
 * Created by John on 8/24/2015.
 */
public class Tab3 extends Fragment {

    private SharedPreferences preferences; // SharedPreferences

    ListView listPlace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_3,container,false);
        listPlace = (ListView) v.findViewById(R.id.favor_list);

        preferences = getActivity().getSharedPreferences("Code", Context.MODE_PRIVATE);
        String codeID = preferences.getString("CodeID","");

        Toast.makeText(getActivity(),codeID,Toast.LENGTH_SHORT).show();
        /*JSON Request */

        JsonArrayRequest favorReq = new JsonArrayRequest(Constant.URL_LIST_PLACE, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        AppController.getInstance().addToRequestQueue(favorReq);
        /*JSON Request */
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferences.edit().remove("CodeID").commit();
    }
}
