package com.example.axgleapi.ui.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

import com.example.axgleapi.R
import com.example.axgleapi.data.Consts
import com.example.axgleapi.data.video.Video
import com.example.axgleapi.data.video.VideoResponse
import com.example.axgleapi.exoplayer.VerticalSpacingItemDecorator
import com.example.axgleapi.exoplayer.VideoPlayerRecyclerAdapter
import com.example.axgleapi.exoplayer.VideoPlayerRecyclerView
import com.example.axgleapi.retrofit.ApiClient
import com.example.axgleapi.utils.Helper
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var param1: String? = null
    private var param2: String? = null
    private var searchKey = ""
    private var mRecyclerView: VideoPlayerRecyclerView? = null
    private var order = "mr"
    private var page = 0
    private var notLoading = true
    private lateinit var arrayListVideos: ArrayList<Video>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: VideoPlayerRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRecyclerView = view.findViewById(com.example.axgleapi.R.id.rcv_search)
        layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.setLayoutManager(layoutManager)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        val itemDecorator = VerticalSpacingItemDecorator(8)
        mRecyclerView!!.addItemDecoration(itemDecorator)

        arrayListVideos = ArrayList<Video>()
        adapter = VideoPlayerRecyclerAdapter(arrayListVideos, initGlide())
        mRecyclerView!!.setAdapter(adapter)

        search_bar.setOnQueryTextListener(this)

        iv_search_order.setOnClickListener {
            if (searchKey.equals("")) {
                Toast.makeText(context, "Please enter search query.", Toast.LENGTH_SHORT).show()
            } else {
                showPopupMenu(requireContext(), it)
            }

        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        this.order = Consts.LATEST
        searchKey = query?.trim().toString()
        text_result.visibility = View.GONE
        spin_kit_rcv_search.visibility = View.VISIBLE
        if (arrayListVideos.size > 0) {
            arrayListVideos.clear()
            adapter.notifyDataSetChanged()
        }

        getVideos(searchKey, page.toString(), order)
        Helper.hideKeyboard(requireActivity())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun showPopupMenu(context: Context, view: View) {
        val popup = PopupMenu(context, view)
        val inflater = popup.getMenuInflater()
        inflater.inflate(com.example.axgleapi.R.menu.menu_ordering_video, popup.getMenu())
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    com.example.axgleapi.R.id.order_latest -> {
                        refreshByOrder(item.title, Consts.LATEST)
                    }
                    com.example.axgleapi.R.id.order_last_viewed -> {
                        refreshByOrder(item.title, Consts.LAST_VIEWED)
                    }
                    com.example.axgleapi.R.id.order_most_viewed -> {
                        refreshByOrder(item.title, Consts.MOST_VIEWED)
                    }
                    com.example.axgleapi.R.id.order_most_favoured -> {
                        refreshByOrder(item.title, Consts.MOST_FAVOURED)
                    }
                    com.example.axgleapi.R.id.order_top_rated -> {
                        refreshByOrder(item.title, Consts.TOP_RATED)
                    }
                    com.example.axgleapi.R.id.order_longest -> {
                        refreshByOrder(item.title, Consts.LONGEST)
                    }
                }
                return false
            }

        })
        popup.show()
    }

    private fun refreshByOrder(title: CharSequence?, orderNew: String) {
        this.order = orderNew
        arrayListVideos.clear()
        mRecyclerView!!.removeAllViews()
        adapter.notifyDataSetChanged()
        spin_kit_rcv_search.visibility = View.VISIBLE
        getVideos(searchKey, this.page.toString(), this.order)
    }


    private fun getVideos(query: String, idx: String, o: String) {
        val call = ApiClient.service.getVideoByKeySearch(query, idx, o, Consts.LIMIT_250)
        call.enqueue(object : Callback<VideoResponse> {
            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    arrayListVideos.addAll(response.body()!!.response.videos as java.util.ArrayList<Video>)
                    mRecyclerView!!.setMediaObjects(arrayListVideos)
                    adapter.notifyDataSetChanged()

                    //                rcv_video.adapter =
                    //                    VideoAdapter(context, response.body()!!.response.videos as MutableList<Video>)
                    spin_kit_rcv_search.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Error..! Please try again", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun initGlide(): RequestManager {
        val options = RequestOptions()
            .placeholder(com.example.axgleapi.R.drawable.white_background)
            .error(com.example.axgleapi.R.drawable.white_background)

        return Glide.with(this)
            .setDefaultRequestOptions(options)
    }

    override fun onDestroy() {
        if (mRecyclerView != null)
            mRecyclerView!!.releasePlayer()
        super.onDestroy()
    }

}
