package com.example.axgleapi.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.axgleapi.R
import com.example.axgleapi.data.collection.Collection
import com.example.axgleapi.utils.Helper
import jp.wasabeef.glide.transformations.internal.Utils
import kotlinx.android.synthetic.main.item_collection.view.*

class CollectionAdapter(
    private val list: ArrayList<Collection>
) : RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_collection, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.onBind(list!![position])
    }


    class CollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(colls: Collection) {
            with(itemView) {
                this.tv_collections_name.text = colls.title
                this.tv_collection_views.text = Helper.parseViews(colls.totalViews.toLong())
                if (colls.videoCount < 250) {
                    this.tv_collection_count.text = "Videos count: ${colls.videoCount}"
                } else {
                    this.tv_collection_count.text = "Videos count: 250"
                }
                Glide.with(this)
                    .load("https://ei.rdtcdn.com/m=e0YH8f/media/videos/202005/21/31785611/original/1.jpg")
//                    .load(colls.coverUrl)
                    .placeholder(R.drawable.ic_photo_size_select_large_black_48dp)
                    .error(R.drawable.ic_photo_size_select_large_black_48dp)
                    .into(this.iv_collections_thumb)

                this.setOnClickListener {
                    Helper.openVideoMoreActivity(itemView.context, colls.title, colls.id)
                    Log.e("collection", colls.id)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

}