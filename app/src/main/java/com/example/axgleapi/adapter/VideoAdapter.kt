package com.example.retrofitredtube.adapter

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.axgleapi.R
import com.example.axgleapi.data.Consts
import com.example.axgleapi.data.video.Video
import com.example.axgleapi.utils.Helper
import kotlinx.android.synthetic.main.item_video.view.*


class VideoAdapter(
    private var context: Context?,
    private var list: MutableList<Video>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_DATA = 0;
        const val VIEW_TYPE_PROGRESS = 1;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_DATA -> {
                VideoHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_video, parent, false)
                )
            }
            VIEW_TYPE_PROGRESS -> {
                ProgressHolder(
                    LayoutInflater.from(
                        parent.context
                    ).inflate(R.layout.item_loading, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Different View type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = list!!.get(position).title
        return when (viewtype) {
            Consts.TITLE_NULL -> VIEW_TYPE_PROGRESS
            else -> VIEW_TYPE_DATA
        }
    }

    fun clearAllItems() {
        list!!.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is VideoHolder) {
            var video = list?.get(position)
            (holder as VideoHolder).bindView(video)
        }
    }

    class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(video: Video?) {

            itemView.tv_video_title.text = video?.title
            itemView.tv_video_date.text = Helper.parseDate(video?.addtime!!)
            itemView.tv_video_views.text = Helper.parseViews(video?.viewnumber!!)
            itemView.tv_video_likes.text = Helper.parseViews(video?.likes!!)
            itemView.tv_video_duration.text = Helper.parseDuration(video?.duration!!)
            Glide.with(itemView.context)
                .load("https://ei.rdtcdn.com/m=e0YH8f/media/videos/202005/21/31785611/original/1.jpg")
//                .load(video?.previewUrl)
                .into(itemView.iv_video_thumb)
            itemView.iv_video_play.setOnClickListener {

                if (itemView.video_container.visibility == View.GONE) {
                    itemView.video_container.visibility = View.VISIBLE
                    itemView.video_preview.setVideoPath(video?.previewVideoUrl);
                    itemView.video_preview.start()
                    itemView.video_preview.setOnCompletionListener(object :
                        MediaPlayer.OnCompletionListener {
                        override fun onCompletion(mp: MediaPlayer?) {
                            if (!mp!!.isPlaying) {
                                itemView.video_container.visibility = View.GONE
                            }

                        }

                    })
                }
            }

            itemView.tv_favorite.setOnClickListener {
                Helper.addVideoToFavorite(video, it)
            }

            itemView.tv_video_full.setOnClickListener {
                Helper.playFullVideo(video?.embeddedUrl, itemView.context)
            }

        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
    }

    class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}