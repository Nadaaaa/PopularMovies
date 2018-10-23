package com.example.hp.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.hp.popularmovies.Models.Movie;
import com.example.hp.popularmovies.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMoviesList;
    private Context mContext;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int ClickedItemIndex);
    }

    public MovieAdapter(Context context, List<Movie> moviesList, ListItemClickListener listener) {
        mContext = context;
        mMoviesList = moviesList;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutMovieItem = R.layout.item_movie;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutMovieItem, viewGroup, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie movie = mMoviesList.get(i);
        movieViewHolder.bind(mContext, movie);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMovieImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieImage = itemView.findViewById(R.id.iv_moviePoster);

            itemView.setOnClickListener(this);
        }

        void bind(Context context, Movie movie) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_placeholder_image);

            Glide.with(context).load(movie.getImage()).thumbnail(0.5f).apply(requestOptions).into(mMovieImage);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
