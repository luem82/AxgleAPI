package com.example.axgleapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.axgleapi.R
import com.example.axgleapi.ui.fragment.FavoriteFragment
import com.example.axgleapi.utils.Helper
import com.example.pexelsretrofitapi.room.FavoriteVideo
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(
    private val fragment: FavoriteFragment,
    private val list: ArrayList<FavoriteVideo>
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {

        return FavoriteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_favorite, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.onBind(list[position], fragment)
    }


    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(
            favoriteVideo: FavoriteVideo,
            fragment: FavoriteFragment
        ) {
            itemView.tv_favorite_title.text = favoriteVideo.title
            itemView.tv_favorite_duration.text = Helper.parseDuration(favoriteVideo.duration)
            Glide.with(itemView)
                .load(R.drawable.ic_photo_size_select_large_black_48dp)
//                .load(favoriteVideo.cover)
                .placeholder(R.drawable.ic_photo_size_select_large_black_48dp)
                .error(R.drawable.ic_photo_size_select_large_black_48dp)
                .into(itemView.iv_favorite_thumb)

            if (!fragment.isContextual) {
                itemView.cb_favorite.visibility = View.GONE
            } else {
                itemView.cb_favorite.visibility = View.VISIBLE
                itemView.cb_favorite.isChecked = false
            }

            itemView.setOnClickListener {
                if (!fragment.isContextual) {
                    Helper.playFullVideo(favoriteVideo.embed, itemView.context)
//                Toast.makeText(itemView.context, "pos - $adapterPosition", Toast.LENGTH_SHORT)
//                    .show()
                }
            }

            itemView.setOnLongClickListener(fragment)

            itemView.cb_favorite.setOnClickListener {
                fragment.MakeSelections(it, adapterPosition)
            }

        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun deletAdapterItems(listDelete: java.util.ArrayList<FavoriteVideo>) {
        for (item: FavoriteVideo in listDelete) {
            list.remove(item)
        }
        notifyDataSetChanged()
    }

}