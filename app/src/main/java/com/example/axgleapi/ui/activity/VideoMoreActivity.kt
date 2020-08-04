package com.example.axgleapi.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
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
import kotlinx.android.synthetic.main.activity_video_more.*
import kotlinx.android.synthetic.main.fragment_video.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoMoreActivity : AppCompatActivity() {

    private lateinit var title: String
    private lateinit var id: String
    private var mRecyclerView: VideoPlayerRecyclerView? = null
    private var order = "mr"
    private var page = 0
    private var notLoading = true
    private lateinit var arrayListVideos: ArrayList<Video>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: VideoPlayerRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_video_more)

        title = intent.getStringExtra(Consts.INTENT_NAME)
        id = intent.getStringExtra(Consts.INTENT_CHID)
        tv_video_more_title.text = title
        tv_video_more_order.text = ": Latest"

        initRecyclerView()

        iv_video_more_order.setOnClickListener {
            showPopupMenu(this, it)
        }

        iv_video_more_refresh.setOnClickListener {
            refreshList()
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = findViewById(R.id.rcv_video_more)
        layoutManager = LinearLayoutManager(this)
        mRecyclerView!!.setLayoutManager(layoutManager)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        val itemDecorator = VerticalSpacingItemDecorator(8)
        mRecyclerView!!.addItemDecoration(itemDecorator)
        arrayListVideos = ArrayList<Video>()
        adapter = VideoPlayerRecyclerAdapter(arrayListVideos, initGlide())
        mRecyclerView!!.setAdapter(adapter)
        loadVideos(page.toString(), order, Consts.LIMIT_250, id)
    }

    private fun loadVideos(page: String, order: String, limit250: Int, id: String) {
        val call = ApiClient.service.getVideoByCategoryID(page, id.toInt(), order, limit250)
        call.enqueue(object : Callback<VideoResponse> {
            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                Toast.makeText(this@VideoMoreActivity, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    arrayListVideos.addAll(response.body()!!.response.videos as java.util.ArrayList<Video>)
                    mRecyclerView!!.setMediaObjects(arrayListVideos)
                    adapter.notifyDataSetChanged()

                    //                rcv_video.adapter =
                    //                    VideoAdapter(context, response.body()!!.response.videos as MutableList<Video>)
                    spin_kit_rcv_video_more.visibility = View.GONE
                } else {
                    Toast.makeText(
                        this@VideoMoreActivity, "Error..! Please try again", Toast.LENGTH_LONG
                    ).show()
                }
            }

        })
    }

    private fun showPopupMenu(context: Context, view: View) {
        val popup = PopupMenu(context, view)
        val inflater = popup.getMenuInflater()
        inflater.inflate(com.example.axgleapi.R.menu.menu_ordering_video, popup.getMenu())
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.itemId) {
                    com.example.axgleapi.R.id.order_latest -> {
                        refreshByOrder(Consts.LATEST)
                    }
                    com.example.axgleapi.R.id.order_last_viewed -> {
                        refreshByOrder(Consts.LAST_VIEWED)
                    }
                    com.example.axgleapi.R.id.order_most_viewed -> {
                        refreshByOrder(Consts.MOST_VIEWED)
                    }
                    com.example.axgleapi.R.id.order_most_favoured -> {
                        refreshByOrder(Consts.MOST_FAVOURED)
                    }
                    com.example.axgleapi.R.id.order_top_rated -> {
                        refreshByOrder(Consts.TOP_RATED)
                    }
                    com.example.axgleapi.R.id.order_longest -> {
                        refreshByOrder(Consts.LONGEST)
                    }
                }
                return false
            }

        })
        popup.show()
    }

    private fun refreshByOrder(orderNew: String) {
        tv_video_more_order.text = ": $orderNew"
        this.order = orderNew
        this.page = 0
        arrayListVideos.clear()
        mRecyclerView!!.removeAllViews()
        adapter.notifyDataSetChanged()
        spin_kit_rcv_video_more.visibility = View.VISIBLE
        loadVideos(this.page.toString(), this.order, Consts.LIMIT_50, this.id)
    }

    private fun refreshList() {
        this.page = 0
        arrayListVideos.clear()
        mRecyclerView!!.removeAllViews()
        adapter.notifyDataSetChanged()
        spin_kit_rcv_video_more.visibility = View.VISIBLE
        loadVideos(this.page.toString(), this.order, Consts.LIMIT_50, this.id)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.act_1_in, R.anim.act_2_out)
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
