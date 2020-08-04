package com.example.axgleapi.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.axgleapi.R
import com.example.axgleapi.data.Consts
import com.example.axgleapi.data.video.Video
import com.example.axgleapi.ui.activity.FullVideoActivity
import com.example.axgleapi.ui.activity.HomeActivity.Companion.appDatabase
import com.example.axgleapi.ui.activity.VideoMoreActivity
import com.example.pexelsretrofitapi.room.FavoriteVideo
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Helper {

    fun parseDuration(value: Double): String {

        val format = SimpleDateFormat("HHmmss")
        val intValueStr = value.toInt().toString()
        val length = intValueStr.length
        val missingDigits = 6 - length
        var strForTimeParsing = intValueStr
        for (i in 0 until missingDigits) {
            strForTimeParsing = "0$strForTimeParsing"
        }
        var parsedDate: Date? = null
        try {
            parsedDate = format.parse(strForTimeParsing)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return SimpleDateFormat("HH:mm:ss").format(parsedDate!!)

    }

    fun parseViews(views: Long): String {
        val format = DecimalFormat("#,###")
        return format.format(views)
    }

    fun parseDate(value: Long): String {
        val date = Date(value * 1000L)
        val df2 = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss")
        return df2.format(date)
    }

    fun addVideoToFavorite(video: Video, v: View) {
        Log.e("addVideoToFavorite", "Video Added")

        val favorite = FavoriteVideo(
            video.addtime, video.previewUrl,
            video.title, video.embeddedUrl, video.duration
        )
        appDatabase!!.getDatabase().insertVideo(favorite)
        Snackbar.make(v, "Added successfully.", Snackbar.LENGTH_SHORT).show()
    }

    fun playFullVideo(embeddedUrl: String, context: Context?) {
        val intent = Intent(context, FullVideoActivity::class.java)
        intent.putExtra("embeddedUrl", embeddedUrl)
        context?.startActivity(intent)
        (context as Activity).overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
    }

    fun hideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm!!.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    fun openVideoMoreActivity(context: Context?, name: String, chID: String) {
        val intent = Intent(context, VideoMoreActivity::class.java)
        intent.putExtra(Consts.INTENT_NAME, name)
        intent.putExtra(Consts.INTENT_CHID, chID)
        context?.startActivity(intent)
        (context as Activity).overridePendingTransition(R.anim.act_2_in, R.anim.act_1_out)
    }

}