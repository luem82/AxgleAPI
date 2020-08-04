package com.example.axgleapi.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.axgleapi.R
import com.example.axgleapi.adapter.CategoryAdapter
import com.example.axgleapi.adapter.CollectionAdapter
import com.example.axgleapi.data.Consts
import com.example.axgleapi.data.category.Category
import com.example.axgleapi.data.category.CategoryResponse
import com.example.axgleapi.data.collection.Collection
import com.example.axgleapi.data.collection.CollectionsResponse
import com.example.axgleapi.exoplayer.VerticalSpacingItemDecorator
import com.example.axgleapi.retrofit.ApiClient
import kotlinx.android.synthetic.main.fragment_category.*
import kotlinx.android.synthetic.main.fragment_collection.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CollectionFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var layoutManager = LinearLayoutManager(context)
        rcv_collections.layoutManager = layoutManager
        rcv_collections.setHasFixedSize(true)
        rcv_collections.itemAnimator = DefaultItemAnimator()
        val itemDecorator = VerticalSpacingItemDecorator(8)
        rcv_collections!!.addItemDecoration(itemDecorator)

        var arrayList = arrayListOf<Collection>()
        var adapter = CollectionAdapter(arrayList)
        rcv_collections.adapter = adapter

        var call = ApiClient.service.getColections("0", Consts.LIMIT_250)
        call.enqueue(object : Callback<CollectionsResponse> {
            override fun onFailure(call: Call<CollectionsResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CollectionsResponse>, response: Response<CollectionsResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    arrayList.addAll(response.body()!!.response.collections)
                    adapter.notifyDataSetChanged()
                    spin_kit_rcv_collections.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Error...", Toast.LENGTH_SHORT).show()
                }
            }

        })

        iv_collectoins_refresh.setOnClickListener {
            arrayList.shuffle()
            adapter.notifyDataSetChanged()
        }
    }

}
