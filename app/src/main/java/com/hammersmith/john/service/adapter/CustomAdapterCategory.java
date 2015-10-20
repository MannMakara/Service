package com.hammersmith.john.service.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Category;

import java.util.List;

/**
 * Created by Khmer on 9/14/2015.
 */
public class CustomAdapterCategory extends BaseAdapter {

    private Activity activity;
    private List<Category> categories;
    private int mItemHeight = 0;
    private int mNumColumns = 0;
    private RelativeLayout.LayoutParams mImageViewLayoutParams;
    LayoutInflater inflater;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapterCategory(Activity activity,List<Category> categories){
        this.activity = activity;
        this.categories = categories;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    // set numcols
    public void setNumColumns(int numColumns) {
        mNumColumns = numColumns;
    }

    public int getNumColumns() {
        return mNumColumns;
    }

    // set photo item height
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
        mItemHeight = height;
        mImageViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item,null);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView cover = (NetworkImageView) convertView.findViewById(R.id.cover);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        Category cate = categories.get(position);

        cover.setLayoutParams(mImageViewLayoutParams);

        // Check the height matches our calculated column width
        if (cover.getLayoutParams().height != mItemHeight) {
            cover.setLayoutParams(mImageViewLayoutParams);
        }

        cover.setImageUrl(cate.getImage(), imageLoader);
        title.setText(cate.getName());
        return convertView;
    }
}
