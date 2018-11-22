package com.example.hp.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.popularmovies.Models.Review;
import com.example.hp.popularmovies.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<Review> mReviewsList;
    private Context mContext;

    public ReviewAdapter(Context context, List<Review> reviewsList) {
        mContext = context;
        mReviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutMovieItem = R.layout.item_review;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutMovieItem, viewGroup, false);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        String author = "", content = "";
        if (mReviewsList.get(0) != null) {
            author = mReviewsList.get(i).getAuthor();
            content = mReviewsList.get(i).getContent();
        }
        reviewViewHolder.bind(author, content);
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tv_author, tv_content, tv_noReviews;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_author = itemView.findViewById(R.id.itemReview_author);
            tv_content = itemView.findViewById(R.id.itemReview_content);
            tv_noReviews = itemView.findViewById(R.id.itemReview_noReviews);
        }

        void bind(String author, String content) {
            if (!author.equals("") && !content.equals("")) {
                tv_author.setText(author);
                tv_content.setText(content);
            } else {
                tv_noReviews.setVisibility(View.VISIBLE);
                tv_author.setVisibility(View.GONE);
                tv_content.setVisibility(View.GONE);
            }
        }
    }
}
