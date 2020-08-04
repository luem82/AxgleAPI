package com.example.axgleapi.ui.fragment


import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.axgleapi.R
import com.example.axgleapi.adapter.FavoriteAdapter
import com.example.axgleapi.ui.activity.HomeActivity.Companion.appDatabase
import com.example.pexelsretrofitapi.room.FavoriteVideo
import kotlinx.android.synthetic.main.fragment_favorite.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavoriteFragment : Fragment(), View.OnLongClickListener {

    private var param1: String? = null
    private var param2: String? = null
    var isContextual = false
    lateinit var listDelete: ArrayList<FavoriteVideo>
    lateinit var list: ArrayList<FavoriteVideo>
    lateinit var adapter: FavoriteAdapter

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
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcv_favorite.layoutManager = LinearLayoutManager(context)
        rcv_favorite.setHasFixedSize(true)
        rcv_favorite.itemAnimator = DefaultItemAnimator()

        listDelete = arrayListOf()
        list = appDatabase!!.getDatabase().getAllVides() as ArrayList<FavoriteVideo>
        adapter = FavoriteAdapter(this, list)
        rcv_favorite.adapter = adapter

        if (list.size > 0) {
            tv_empty.visibility = View.GONE
        }
    }


    override fun onLongClick(view: View?): Boolean {

        isContextual = true
        favorite_toolbar.inflateMenu(R.menu.menu_favorite_video)
        favorite_toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp)
        adapter.notifyDataSetChanged()

        favorite_toolbar.setNavigationOnClickListener {
            favorite_toolbar.menu.clear()
            favorite_toolbar.setNavigationIcon(null)
            tv_favorite_delete.text = "Favorites"
            isContextual = false
            adapter.notifyDataSetChanged()
        }

        favorite_toolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                showDialogDelete()
                return true
            }

        })
        return true
    }

    fun MakeSelections(v: View?, position: Int) {
        if ((v as CheckBox).isChecked) {
            listDelete.add(list[position])
            tv_favorite_delete.text = "${listDelete.size} video selected"
        } else {
            listDelete.remove(list[position])
            tv_favorite_delete.text = "${listDelete.size} video selected"
        }
    }

    private fun showDialogDelete() {
        var builde = android.app.AlertDialog.Builder(context)
            .setIcon(R.drawable.ic_delete_forever_black_24dp)
            .setTitle("Delete all")
            .setMessage("Are you sure you want to delete?")
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }

            })
            .setPositiveButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    deleteAllVideoSelected()
                    dialog?.dismiss()
                }

            })

        var dialog = builde.create()
        dialog.show()
    }

    private fun deleteAllVideoSelected() {
        adapter.deletAdapterItems(listDelete)
        deleteFromDatabase(listDelete)
        listDelete.clear()
        favorite_toolbar.menu.clear()
        favorite_toolbar.setNavigationIcon(null)
        tv_favorite_delete.text = "Favorites"
        isContextual = false
        adapter.notifyDataSetChanged()
        if (list.size > 0) {
            tv_empty.visibility = View.GONE
        } else {
            tv_empty.visibility = View.VISIBLE
        }
        Toast.makeText(context, "Delete successfully.", Toast.LENGTH_SHORT).show()
    }

    private fun deleteFromDatabase(listDelete: java.util.ArrayList<FavoriteVideo>) {
        var roomDAO = appDatabase!!.getDatabase()
        for (item: FavoriteVideo in listDelete) {
            roomDAO.deleteVideo(item)
        }
    }

}
