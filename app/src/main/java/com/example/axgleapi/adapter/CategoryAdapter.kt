package com.example.axgleapi.adapter

import android.content.Intent
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
import com.example.axgleapi.data.category.Category
import com.example.axgleapi.utils.Helper
import jp.wasabeef.glide.transformations.internal.Utils
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryAdapter(
    private val list: ArrayList<Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_category, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(list[position])
    }


    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(category: Category) {
            itemView.tv_category_name.text = category.name
            Glide.with(itemView)
                .load("https://avgle.com/media/categories/video/21.jpg")
//                .load(category.coverUrl)
                .placeholder(R.drawable.ic_photo_size_select_large_black_48dp)
                .error(R.drawable.ic_photo_size_select_large_black_48dp)
                .into(itemView.iv_category_thumb)

            itemView.setOnClickListener {
                Helper.openVideoMoreActivity(itemView.context, category.name, category.cHID)
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

}