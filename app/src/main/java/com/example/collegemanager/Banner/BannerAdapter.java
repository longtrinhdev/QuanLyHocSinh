package com.example.collegemanager.Banner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.collegemanager.R;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private ArrayList<Banner> lst;
    private ViewPager2 viewPager2;

    private Context context;

    private Runnable runnable =new Runnable() {
        @Override
        public void run() {
            lst.addAll(lst);
            notifyDataSetChanged();
        }
    };

    public BannerAdapter(ArrayList<Banner> lst, ViewPager2 viewPager2, Context context) {
        this.lst = lst;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_holder, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(60));
        if(context != null) {
            Glide.with(context)
                    .load(lst.get(position).getImage())
                    .apply(requestOptions)
                    .into(holder.img_banner);

            if(position == lst.size()-2) {
                viewPager2.post(runnable);
            }
        }

    }

    @Override
    public int getItemCount() {
        if(lst == null) {
            return 0;
        }
        return lst.size();
    }

    public  class BannerViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_banner;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            img_banner = itemView.findViewById(R.id.img_banner);
        }
    }
}
