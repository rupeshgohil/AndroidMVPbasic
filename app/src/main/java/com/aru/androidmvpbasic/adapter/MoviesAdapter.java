/**
 * @file MovieAdapter.java
 * @brief This is an adapter class responsible for showing movies data.
 * @author Shrikant
 * @date 14/04/2018
 */
package com.aru.androidmvpbasic.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.aru.androidmvpbasic.Api.ApiConstant;
import com.aru.androidmvpbasic.R;
import com.aru.androidmvpbasic.listener.MutiClickListener;
import com.aru.androidmvpbasic.modal.Movie;
import com.aru.androidmvpbasic.movielist.MainActivity;
import com.aru.androidmvpbasic.movielist.presenter.MovieListview;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> implements Filterable {

    private MutiClickListener mutiClickListener;
    private MovieListview.ShowEmptyView  mEmptyView;
    private ArrayList<Movie> movieList;
    private ArrayList<Movie> originalMovieList;
    Context mContext;
    private String fromDate;
    private String toDate;

    public MoviesAdapter(MainActivity context, ArrayList<Movie> movieList) {
        Log.e("Adapterdata",new Gson().toJson(movieList));
        this.mContext = context;
        this.mutiClickListener =context;
        this.mEmptyView = context;
        this.movieList = movieList;
        this.originalMovieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.bind(position);


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
    public void setFilterParameter(String fromDate, String toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<Movie> filteredResults = null;
                if (fromDate.isEmpty() || toDate.isEmpty()) {
                    filteredResults = originalMovieList;
                } else {
                    filteredResults = getFilteredResults(fromDate, toDate);
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movieList = (ArrayList<Movie>) filterResults.values;
                MoviesAdapter.this.notifyDataSetChanged();

                if (getItemCount() == 0) {
                    mEmptyView.showEmptyView();
                } else {
                    mEmptyView.hideEmptyView();
                }
            }
        };
    }
    protected List<Movie> getFilteredResults(String fromDate, String toDate) {
        List<Movie> results = new ArrayList<>();

        for (Movie item : originalMovieList) {

            if (item.getReleaseDate().isEmpty()) {
                continue;
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date minDate = null;
            Date maxDate = null;
            Date releaseDate = null;
            try {
                minDate = format.parse(fromDate);
                maxDate = format.parse(toDate);
                releaseDate = format.parse(item.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            if (releaseDate.after(minDate) && releaseDate.before(maxDate)) {
                results.add(item);
            }
        }
        return results;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_movie_title)
        public TextView tvMovieTitle;
        @BindView(R.id.tv_movie_ratings)
        public TextView tvMovieRatings;
        @BindView(R.id.tv_release_date)
        public TextView tvReleaseDate;
        @BindView(R.id.iv_movie_thumb)
        public ImageView ivMovieThumb;
        @BindView(R.id.pb_load_image)
        public ProgressBar pbLoadImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void bind(final int position) {
            Movie movie = movieList.get(position);
            tvMovieTitle.setText(movie.getTitle());
            tvMovieRatings.setText(String.valueOf(movie.getRating()));
            tvReleaseDate.setText(movie.getReleaseDate());

            // loading album cover using Glide library
            Glide.with(mContext)
                    .load(ApiConstant.IMAGE_BASE_URL + movie.getThumbPath())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            pbLoadImage.setVisibility(View.GONE);
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pbLoadImage.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                    .into(ivMovieThumb);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mutiClickListener.onMultiClick("",position,view);
            }
        });
        }
    }
}
