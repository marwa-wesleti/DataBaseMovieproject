package com.marwa.moviesproject.modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.ResultCast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCast extends RecyclerView.Adapter<AdapterCast.myViewHolder>{
    @NonNull
    Context context;
    List<ResultCast> casts;

    public AdapterCast(@NonNull Context context, List<ResultCast> casts) {
        this.context = context;
        this.casts = casts;
    }

    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcast,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        if(casts.get(position).getProfilePath()!=null) {
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + casts.get(position).getProfilePath())
                    .placeholder(R.drawable.prograss_bar)
                    .centerCrop()
                    .into(holder.imgCast);
        }

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgCast;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCast=(CircleImageView) itemView.findViewById(R.id.imageCast);
        }
    }
}
