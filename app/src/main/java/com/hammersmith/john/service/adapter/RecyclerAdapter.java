package com.hammersmith.john.service.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Category;

import java.util.List;

/**
 * Created by Khmer on 9/21/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CategoryViewHolder> {
    private static final String TAG = "CustomAdapter";
    private Activity activity;
    private List<Category> categories;
    Category category;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private ClickListener clickListener;

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        NetworkImageView cover;
        TextView title;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            cover = (NetworkImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            activity.startActivity(new Intent(activity,SpaActivity.class));
            if (clickListener!=null){
                clickListener.itemClicked(v,getLayoutPosition());
            }
        }
    }

    public RecyclerAdapter(Activity activity, List<Category> categories){
        this.activity = activity;
        this.categories = categories;
    }
    @Override
    public RecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cate_item, parent,false);
        CategoryViewHolder CVH = new CategoryViewHolder(v);
        return CVH;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.CategoryViewHolder holder, final int position) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        category = categories.get(position);

        holder.cover.setImageUrl(category.getImage(), imageLoader);
        holder.title.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }
}
