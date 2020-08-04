package com.example.axgleapi.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager

import com.example.axgleapi.R
import com.example.axgleapi.adapter.CategoryAdapter
import com.example.axgleapi.data.category.Category
import com.example.axgleapi.data.category.CategoryResponse
import com.example.axgleapi.retrofit.ApiClient
import kotlinx.android.synthetic.main.fragment_category.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoryFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var layoutManager = GridLayoutManager(context, 2)
        rcv_category.layoutManager = layoutManager
        rcv_category.setHasFixedSize(true)
        rcv_category.itemAnimator = DefaultItemAnimator()
        var arrayList = arrayListOf<Category>()
        var adapter = CategoryAdapter(arrayList)
        rcv_category.adapter = adapter

        var call = ApiClient.service.getCategories()
        call.enqueue(object : Callback<CategoryResponse> {
            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CategoryResponse>, response: Response<CategoryResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    arrayList.addAll(response.body()!!.response.categories)
                    adapter.notifyDataSetChanged()
                    spin_kit_rcv_category.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Error...", Toast.LENGTH_SHORT).show()
                }
            }

        })

        iv_category_refresh.setOnClickListener {
            arrayList.shuffle()
            adapter.notifyDataSetChanged()
        }
    }

}
