package com.example.axgleapi.ui.fragment


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.fragment_video.*
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.axgleapi.data.video.Video
import com.example.axgleapi.data.video.VideoResponse
import com.example.axgleapi.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.RequestManager
import kotlin.collections.ArrayList
import com.example.axgleapi.exoplayer.VideoPlayerRecyclerView
import com.example.axgleapi.exoplayer.VideoPlayerRecyclerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.axgleapi.R
import com.example.axgleapi.data.Consts
import com.example.axgleapi.exoplayer.VerticalSpacingItemDecorator
import com.example.axgleapi.ui.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        return inflater.inflate(com.example.axgleapi.R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        mRecyclerView = view.findViewById(com.example.axgleapi.R.id.rcv_video)
        initRecyclerView();

        im_video_order.setOnClickListener {
            showPopupMenu(requireContext(), it)
        }

        im_video_refresh.setOnClickListener {
            refreshList()
        }
    }

    private fun initRecyclerView() {

        layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.setLayoutManager(layoutManager)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        val itemDecorator = VerticalSpacingItemDecorator(8)
        mRecyclerView!!.addItemDecoration(itemDecorator)

        arrayListVideos = ArrayList<Video>()
        adapter = VideoPlayerRecyclerAdapter(arrayListVideos, initGlide())
        mRecyclerView!!.setAdapter(adapter)

        loadVideos(page.toString(), order, Consts.LIMIT_50)
        addLoadMoreScroll()

    }

    private fun loadVideos(page: String, order: String, videoCount: Int) {
        val call = ApiClient.service.getVideoByOrdering(page, order, videoCount)
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
                    spin_kit_rcv_video.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Error..! Please try again", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun addLoadMoreScroll() {
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (notLoading && layoutManager.findLastCompletelyVisibleItemPosition() == arrayListVideos.size - 1) {

                    //add null
                    page += 1
                    arrayListVideos.add(Consts.VIDEO_NULL)
                    adapter.notifyItemInserted(arrayListVideos.size - 1)
                    notLoading = false

                    // call apiService
                    val call =
                        ApiClient.service.getVideoByOrdering(
                            page.toString(),
                            order,
                            Consts.LIMIT_50
                        )
                    call!!.enqueue(object : Callback<VideoResponse> {

                        override fun onResponse(
                            call: Call<VideoResponse>?, response: Response<VideoResponse>?
                        ) {
                            // remove load more item
                            arrayListVideos.removeAt(arrayListVideos.size - 1)
                            adapter.notifyItemRemoved(arrayListVideos.size)

                            if (response!!.isSuccessful && response.body() != null) {
                                if (arrayListVideos.size < response.body()!!.response.totalVideos) {
                                    // load more video
                                    arrayListVideos.addAll(response.body()!!.response.videos as java.util.ArrayList<Video>)
                                    adapter.notifyDataSetChanged()
                                    notLoading = true
                                } else if (arrayListVideos.size == response.body()!!.response.totalVideos) {
                                    Toast.makeText(
                                        context, "End of videos list.", Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                Toast.makeText(context, "Data is null.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<VideoResponse>?, t: Throwable?) {
                            Toast.makeText(context, t?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    })


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
        tv_video_order.text = title
        this.order = orderNew
        this.page = 0
        arrayListVideos.clear()
        mRecyclerView!!.removeAllViews()
        adapter.notifyDataSetChanged()
        spin_kit_rcv_video.visibility = View.VISIBLE
        loadVideos(this.page.toString(), this.order, Consts.LIMIT_50)
        addLoadMoreScroll()
    }

    private fun refreshList() {
        this.page = 0
        arrayListVideos.clear()
        mRecyclerView!!.removeAllViews()
        adapter.notifyDataSetChanged()
        spin_kit_rcv_video.visibility = View.VISIBLE
        loadVideos(this.page.toString(), this.order, Consts.LIMIT_50)
        addLoadMoreScroll()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    }

}
