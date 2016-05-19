package com.hammersmith.john.service.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Place;

import java.util.List;

/**
 * Created by Khmer on 9/14/2015.
 */
public class CustomAdapterPlace extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Place> placeItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapterPlace(Activity activity, List<Place> places){
        this.activity = activity;
        this.placeItems = places;
    }

    @Override
    public int getCount() {
        return placeItems.size();
    }

    @Override
    public Object getItem(int position) {
        return placeItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.custom_place_layout,null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView image = (NetworkImageView) convertView.findViewById(R.id.logo);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView address = (TextView) convertView.findViewById(R.id.addr);
        TextView dis = (TextView) convertView.findViewById(R.id.discount);


        Place place = placeItems.get(position);
        //set Logo
        image.setImageUrl(place.getImage(),imageLoader);
        //Set Title of Place
        title.setText(place.getName());
        //Set Address of the Place
        address.setText(place.getAddress());
        //Set discount
        if (place.getDis() == 0)
            dis.setText("N/A");
        else
            dis.setText(place.getDis()+"% Off");
        return convertView;
    }
}
