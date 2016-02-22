package com.hammersmith.john.service.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.adapter.CustomAdapterCategory;
import com.hammersmith.john.service.adapter.RecyclerAdapter;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.Constant;

/**
 * Created by John on 8/24/2015.
 */
public class Tab1 extends Fragment implements RecyclerAdapter.ClickListener{

    ProgressDialog progressDialog;
    ViewFlipper viewFlipper;
    GestureDetector mGestureDetector;
    TextView hotel;
    TextView spa;
    TextView resta;
    int[] id;
    String[] title;
    int[] re = {
            R.drawable.selantra_restaurant,
            R.drawable.text_mot,
            R.drawable.logo,
            R.drawable.sb
    };

    // Test GridView //
    RecyclerView recyclerView;

//    GridView gridView;
//    private int mPhotoSize, mPhotoSpacing;

    List<Category> categories = new ArrayList<Category>();
    Category cate;
    CustomAdapterCategory adapterCategory;

    RecyclerAdapter adapter;

    // FInish //

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_1, container, false);
//        categories.clear();
        viewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipper);
        // ********** Recycler View ****************//
        recyclerView = (RecyclerView) v.findViewById(R.id.cate);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });

        recyclerView.setLayoutManager(manager);

        adapter = new RecyclerAdapter(getActivity(),categories);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);
        // ********** Recycler View ****************//

        if (categories.size() <= 0){
            showProgress();
            // Creating volley request obj
            JsonArrayRequest cateReq = new JsonArrayRequest(Constant.URL_CATEGORY, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    hideProgress();
                    id = new int[jsonArray.length()];
                    title = new String[jsonArray.length()];
                    for (int i=0; i< jsonArray.length() ; i++) {
                        try {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            cate = new Category();
                            cate.setImage(Constant.URL_INDEX + obj.getString("image_path"));
                            cate.setName(obj.getString("category_name"));
                            id[i] = obj.getInt("id");
                            title[i] = obj.getString("category_name");
                            categories.add(cate);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // notifying list adapter about data changes
                    // so that it renders the list view with updated data
//                adapterCategory.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity(),volleyError+"",Toast.LENGTH_SHORT).show();
                }
            });
            AppController.getInstance().addToRequestQueue(cateReq);

        }


        // Test //
//        gridView = (GridView) v.findViewById(R.id.grid);
//
//        mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
//        mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
//        adapterCategory = new CustomAdapterCategory(getActivity(),categories);
//        gridView.setAdapter(adapterCategory);
//
//        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (adapterCategory.getNumColumns() == 0) {
//                    final int numColumn = (int) Math.floor(gridView.getWidth() / (mPhotoSize + mPhotoSpacing));
//                    if (numColumn > 0) {
//                        final int columnWidth = (gridView.getWidth() / numColumn) - mPhotoSpacing;
//                        adapterCategory.setNumColumns(numColumn);
//                        adapterCategory.setItemHeight(columnWidth);
//                    }
//                }
//            }
//        });

        // Finish //

        // *** Click on postion *****//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
//                Intent intent = new Intent(getActivity(),SpaActivity.class);
//                intent.putExtra("id",id[position]);
//                Toast.makeText(getActivity(),"Position:"+id[position],Toast.LENGTH_LONG).show();
//                startActivity(intent);
//            }
//        });
        // *** Click on postion *****//

        for (int i = 0; i < re.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(re[i]);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setInAnimation(getActivity(), R.anim.left_in);
        viewFlipper.setOutAnimation(getActivity(), R.anim.left_out);
        viewFlipper.showNext();
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(getActivity(),customGestureDetector);
        return v;

    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent(getActivity(),SpaActivity.class);
        intent.putExtra("id", id[position]+"");
        intent.putExtra("title",title[position]);
//        Toast.makeText(getActivity(),"Position:"+id[position],Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(),"Title:"+title[position],Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                viewFlipper.setInAnimation(getActivity(), R.anim.left_in);
                viewFlipper.setOutAnimation(getActivity(), R.anim.left_out);
                viewFlipper.showNext();
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {

                viewFlipper.setInAnimation(getActivity(), R.anim.right_in);
                viewFlipper.setOutAnimation(getActivity(), R.anim.right_out);
                viewFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
