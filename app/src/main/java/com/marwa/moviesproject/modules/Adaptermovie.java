package com.marwa.moviesproject.modules;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.GenreMovie;
import com.marwa.moviesproject.models.PageDetails;
import com.marwa.moviesproject.models.PageMovies;
import com.marwa.moviesproject.models.ResultCast;
import com.marwa.moviesproject.models.ResultMovie;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;

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
        holder.nom.setText(movies.get(position).getName());
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
            List<ResultMovie> filtertolist = new ArrayList<>();// declarit list bch nhot feha film filté
            if (charSequence.toString().isEmpty()) { //testit ken bulle de recherche vide ykhali liste kima hya
                filtertolist.addAll(movieListAll);

            } else {
                for (ResultMovie movie : movieListAll) {
                    if (movie.getName().toLowerCase().contains(charSequence.toString().toLowerCase()) ){ //yparcouri fel liste aflem w ytesti ken nom fil contient carractére  fel bulle de recherche
                        filtertolist.add(movie);
                    }
                }
            }
            FilterResults filterResults=new FilterResults(); // bch nhot resultat fi variable
            filterResults.values=filtertolist;
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
