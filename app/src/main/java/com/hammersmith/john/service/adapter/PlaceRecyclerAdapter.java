package com.hammersmith.john.service.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.app.PlaceDetailActivity;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Place;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Constant;

/**
 * Created by Khmer on 10/2/2015.
 */
public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceRecyclerAdapter.ViewHolder> {

    LayoutInflater inflater;
    Place place;

    public PlaceRecyclerAdapter(Context context,Place place){
        inflater = LayoutInflater.from(context);
        this.place = place;
    }

    public PlaceRecyclerAdapter(Context context){
        inflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.place_detail,parent,false);
        ViewHolder holder = new ViewHolder(parent.getContext(),root);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int id_detail = PlaceDetailActivity.placeID;
        /*JsonObjectRequest*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constant.URL_PLACE + id_detail, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    holder.txtDetail.setText(Html.fromHtml(jsonObject.getString("customer_desc")));
                    holder.txtFee.setText(jsonObject.getString("fee"));
                    holder.txtMail.setText(jsonObject.getString("email"));
                    holder.txtPhone.setText(jsonObject.getString("phone"));
                    holder.txtWeb.setText(jsonObject.getString("website"));
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
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView txtDetail,txtPhone,txtWeb,txtMail,txtFee;
        ImageView imgPhone,imgWeb,imgMail,imgLocate;
        Button btnBookMe;
        TableRow trCall;
        MapView mapView;
        GoogleMap mGoogleMap;
        Context mContext;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            mContext = context;
            txtDetail = (TextView) itemView.findViewById(R.id.detail);
            txtPhone = (TextView) itemView.findViewById(R.id.txtPhone);
            txtMail = (TextView) itemView.findViewById(R.id.txtMail);
            txtWeb = (TextView) itemView.findViewById(R.id.txtWeb);
            txtFee = (TextView) itemView.findViewById(R.id.txtFee);

            imgPhone = (ImageView) itemView.findViewById(R.id.imgPhone);
            imgMail = (ImageView) itemView.findViewById(R.id.imgMail);
            imgWeb = (ImageView) itemView.findViewById(R.id.imgWeb);
//            imgLocate = (ImageView) itemView.findViewById(R.id.imgLocate);

            trCall = (TableRow) itemView.findViewById(R.id.call);

            btnBookMe = (Button) itemView.findViewById(R.id.btnBookMe);

            mapView = (MapView) itemView.findViewById(R.id.map);

            mapView.onCreate(null);
            mapView.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            MapsInitializer.initialize(mContext);
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(11.570937,104.937177)));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(11.570937,104.937177),16f);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }
}
