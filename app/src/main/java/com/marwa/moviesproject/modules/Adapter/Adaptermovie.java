package com.marwa.moviesproject.modules.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.Home.ResultMovie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Adaptermovie extends RecyclerView.Adapter<Adaptermovie.myViewHolder> implements Filterable {
    @NonNull
    Context context;
    List<ResultMovie> movies;
    OnItemSelectedListener onItemSelectedListener;
    List<ResultMovie> movieListAll;



    public Adaptermovie(@NonNull Context context, List<ResultMovie> movies,OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.movies = movies;
        this.onItemSelectedListener=onItemSelectedListener;
        this.movieListAll=new ArrayList<>(movies);// declarina liste feha les donnée kol
    }



    @Override
    public Adaptermovie.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);

        return new myViewHolder(v,onItemSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adaptermovie.myViewHolder holder, int position) {


        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+movies.get(position).getPosterPath())
                .placeholder(R.drawable.prograss_bar)
                .centerCrop()
                .into(holder.img);
        if (movies.get(position).getTitle() == null || movies.get(position).getTitle().trim().isEmpty()) {
            // getTitle est vide, utiliser getName à la place
            holder.nom.setText(movies.get(position).getName());
        } else {
            // Utiliser getTitle car il n'est pas vide
            holder.nom.setText(movies.get(position).getTitle());
        }
        holder.rate.setText(""+(Math.round(movies.get(position).getVoteAverage()))*10+"%");



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ResultMovie> filtertolist = new ArrayList<>(); // Initialisation de la liste filtrée
            if (charSequence == null || charSequence.length() == 0) {
                // La chaîne de recherche est vide, renvoyer la liste complète
                filtertolist.addAll(movieListAll);
            } else {
                // Parcourir la liste complète pour filtrer les éléments correspondants
                for (ResultMovie movie : movieListAll) {
                    if (movie.getTitle() != null && movie.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtertolist.add(movie);
                    } else if (movie.getName() != null && movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtertolist.add(movie);
                    }
                }
            }

            // Créer le résultat du filtrage
            FilterResults filterResults = new FilterResults();
            filterResults.values = filtertolist;
            filterResults.count = filtertolist.size(); // Définir le nombre d'éléments dans la liste filtrée

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            movies.clear();
            movies.addAll((Collection<? extends ResultMovie>) filterResults.values); // bch tfasakh liste 9dima w t'affichie list filtré
            notifyDataSetChanged();

        }
    };




public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        ImageView img;
        OnItemSelectedListener onItemSelectedListener;
        TextView nom,rate;


        public myViewHolder(@NonNull View itemView, OnItemSelectedListener onItemSelectedListener) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            nom=(TextView) itemView.findViewById(R.id.nom);
            rate=(TextView) itemView.findViewById(R.id.rate);
            this.onItemSelectedListener = onItemSelectedListener;
            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            onItemSelectedListener.itemClick(getAdapterPosition());

        }
    }
    public interface OnItemSelectedListener{
        void itemClick(int position);

    }
}
