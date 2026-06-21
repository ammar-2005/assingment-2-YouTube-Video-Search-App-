package com.example.youtubevideosearchapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context context;
    private List<VideoModel> videoList;

    public VideoAdapter(Context context, List<VideoModel> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // تم تغيير item_video إلى activity_video لأن الملف موجود بهذا الاسم ويحتوي على تصميم العنصر
        View view = LayoutInflater.from(context).inflate(R.layout.activity_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoModel video = videoList.get(position); // تصحيح الوصول للعنصر في القائمة
        holder.tvTitle.setText(video.getTitle());
        holder.tvChannel.setText(video.getChannelTitle());
        holder.tvDescription.setText(video.getDescription());
        holder.tvPublishTime.setText(video.getPublishTime());

        // استخدام جلايد لعرض الصورة
        Glide.with(context)
                .load(video.getThumbnailUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView tvTitle, tvChannel, tvDescription, tvPublishTime;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvChannel = itemView.findViewById(R.id.tvChannel);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPublishTime = itemView.findViewById(R.id.tvPublishTime);
        }
    }
}