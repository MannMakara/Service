package com.hammersmith.john.service.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hammersmith.john.service.R;
import com.hammersmith.john.service.roundImage.RoundImage;

/**
 * Created by John on 8/25/2015.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    RoundImage roundImage;

    public CustomAdapter(Context context, String[] places){
        super(context, R.layout.custom_list,places);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customeView = layoutInflater.inflate(R.layout.custom_list, parent, false);
        String singlePlace = getItem(position);
        TextView textView = (TextView) customeView.findViewById(R.id.place_title);
        ImageView imageView = (ImageView) customeView.findViewById(R.id.logo_icon);
        Bitmap bm = BitmapFactory.decodeResource(customeView.getResources(), R.drawable.sokha);
        roundImage = new RoundImage(bm);
        textView.setText(singlePlace);
        imageView.setImageDrawable(roundImage);
        return customeView;
    }
}
