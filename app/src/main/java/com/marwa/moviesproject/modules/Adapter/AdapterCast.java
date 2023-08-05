package com.marwa.moviesproject.modules.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.Casts.ResultCast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterCast extends RecyclerView.Adapter<AdapterCast.myViewHolder>{
    @NonNull
    Context context;
    List<ResultCast> casts;
    Adaptermovie.OnItemSelectedListener onItemSelectedListener;

    public AdapterCast(@NonNull Context context, List<ResultCast> casts, Adaptermovie.OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.casts = casts;
        this.onItemSelectedListener=onItemSelectedListener;
    }

    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcast,parent,false);
        return new myViewHolder(v,onItemSelectedListener);
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
        holder.nameTxt.setText(casts.get(position).getName());
        holder.nameCaracter.setText(casts.get(position).getCharacter());

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircleImageView imgCast;
        TextView nameTxt;
        TextView nameCaracter;

        Adaptermovie.OnItemSelectedListener onItemSelectedListener;

        public myViewHolder(@NonNull View itemView, Adaptermovie.OnItemSelectedListener onItemSelectedListener) {
            super(itemView);
            imgCast=(CircleImageView) itemView.findViewById(R.id.imageCast);
            nameTxt=(TextView) itemView.findViewById(R.id.nameActor);
            nameCaracter=(TextView) itemView.findViewById(R.id.nameCaracter);

            this.onItemSelectedListener=onItemSelectedListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemSelectedListener.itemClick(getAdapterPosition());
        }
    }
}
