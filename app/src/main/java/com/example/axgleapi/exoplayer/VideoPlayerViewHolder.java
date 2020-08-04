package com.example.axgleapi.exoplayer;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.axgleapi.R;
import com.example.axgleapi.data.video.Video;
import com.example.axgleapi.utils.Helper;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    FrameLayout media_container;
    LinearLayout options_container;
    Button fullVideo, addFavorite;
    TextView title, date, views, likes, dislikes, rate, duration, privateVideo;
    ImageView thumbnail, volumeControl, hdVideo;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        options_container = itemView.findViewById(R.id.options_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        date = itemView.findViewById(R.id.date);
        views = itemView.findViewById(R.id.views);
        privateVideo = itemView.findViewById(R.id.private_video);
        likes = itemView.findViewById(R.id.likes);
        duration = itemView.findViewById(R.id.duration);
        fullVideo = itemView.findViewById(R.id.btn_full_video);
        dislikes = itemView.findViewById(R.id.dis_likes);
        rate = itemView.findViewById(R.id.rate);
        addFavorite = itemView.findViewById(R.id.btn_add_favorite);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);
        hdVideo = itemView.findViewById(R.id.hd_video);
    }

    public void onBind(Video video, RequestManager requestManager) {
        this.requestManager = requestManager;
        parent.setTag(this);
        title.setText(video.getTitle());
        date.setText("Published: " + Helper.INSTANCE.parseDate(video.getAddtime()));
        views.setText(Helper.INSTANCE.parseViews(video.getViewnumber()));
        likes.setText(Helper.INSTANCE.parseViews(video.getLikes()));
        dislikes.setText(Helper.INSTANCE.parseViews(video.getDislikes()));
        rate.setText(video.getFramerate() + "");
        duration.setText(Helper.INSTANCE.parseDuration(video.getDuration()));

        if (!video.getHd()) {
            hdVideo.setVisibility(View.INVISIBLE);
        }

        if (video.getPrivate()) {
            options_container.setVisibility(View.GONE);
        } else {
            privateVideo.setVisibility(View.GONE);
        }

        this.requestManager
                .load("https://ei.rdtcdn.com/m=e0YH8f/media/videos/202005/21/31785611/original/1.jpg")
//                .load(video.getPreviewUrl())
                .into(thumbnail);

        fullVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.INSTANCE.playFullVideo(video.getEmbeddedUrl(), parent.getContext());
            }
        });

        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.INSTANCE.addVideoToFavorite(video,v);
            }
        });
    }

}
