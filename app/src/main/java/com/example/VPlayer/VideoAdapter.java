package com.example.VPlayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    public OnItemClickListener onItemClickListener;
    Context mContext;
    ArrayList<Video> mVideoArrayList;

    public VideoAdapter(Context context, ArrayList<Video> videoArrayList) {
        this.mContext = context;
        this.mVideoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoAdapter.ViewHolder holder, int position) {
        holder.videoTitle.setText(mVideoArrayList.get(position).getVideoTitle());
        holder.videoDuration.setText(mVideoArrayList.get(position).getVideoDuration());
    }

    @Override
    public int getItemCount() {
        return mVideoArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView videoTitle;
        TextView videoDuration;

        public ViewHolder(View itemView) {
            super(itemView);

            videoTitle = itemView.findViewById(R.id.title);
            videoDuration = itemView.findViewById(R.id.duration);

            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAbsoluteAdapterPosition(), v));
        }

    }
}