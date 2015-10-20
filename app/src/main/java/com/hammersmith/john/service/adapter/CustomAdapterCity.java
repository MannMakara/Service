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
import com.hammersmith.john.service.app.CustomAdapter;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.City;
import com.hammersmith.john.service.model.Place;
import com.hammersmith.john.service.roundImage.CircledNetworkImageView;

import java.util.List;

/**
 * Created by Khmer on 9/14/2015.
 */
public class CustomAdapterCity extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<City> cityItems;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapterCity(Activity activity,List<City> cities){
        this.activity = activity;
        this.cityItems = cities;
    }


    @Override
    public int getCount() {
        return cityItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cityItems.get(position);
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
            convertView = inflater.inflate(R.layout.custom_list,null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        CircledNetworkImageView image = (CircledNetworkImageView) convertView.findViewById(R.id.logo_icon);
        TextView title = (TextView) convertView.findViewById(R.id.place_title);

        City city = cityItems.get(position);
        //set Logo
        image.setImageUrl(city.getImage(),imageLoader);
        //Set Title of City
        title.setText(city.getName());
        return convertView;
    }
}
