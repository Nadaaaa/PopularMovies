package com.example.hp.popularmovies.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.popularmovies.Models.Video;
import com.example.hp.popularmovies.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private static final String TAG = VideoAdapter.class.getSimpleName();

    private List<Video> mVideosList;
    private Context mContext;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int ClickedItemIndex);
    }

    public VideoAdapter(Context context, List<Video> videosList, ListItemClickListener listener) {
        mContext = context;
        mVideosList = videosList;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutMovieItem = R.layout.item_video;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutMovieItem, viewGroup, false);
        VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int i) {
        String trailer = mVideosList.get(i).getName();
        videoViewHolder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_trailer;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_trailer = itemView.findViewById(R.id.itemVideo_trailerTitle);

            itemView.setOnClickListener(this);
        }

        void bind(String trailer) {
            tv_trailer.setText(trailer);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }
}
