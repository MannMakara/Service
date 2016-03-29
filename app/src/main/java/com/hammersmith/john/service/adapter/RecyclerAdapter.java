package com.hammersmith.john.service.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.controller.AppController;
import com.hammersmith.john.service.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CategoryViewHolder> {
    protected Activity activity;
    private List<Category> categories;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private ClickListener clickListener;

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView cover;
        private TextView title;
        protected CardView cardView;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card);
            cover = (ImageView) itemView.findViewById(R.id.cate_cover);
            title = (TextView) itemView.findViewById(R.id.cate_title);
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

    public RecyclerAdapter(List<Category> categories){
        this.categories = categories;
    }
    @Override
    public RecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cate_item, parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.CategoryViewHolder holder, final int position) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        Category category = categories.get(position);

//        holder.cover.setImageUrl(category.getImage(), imageLoader);
        Uri uri = Uri.parse(category.getImage());
        Context context = holder.cover.getContext();
        Picasso.with(context).load(uri).into(holder.cover);
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
        void itemClicked(View view, int position);
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }
}
