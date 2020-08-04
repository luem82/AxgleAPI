package com.example.axgleapi.ui.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.example.axgleapi.R
import com.github.ybq.android.spinkit.SpinKitView

class FullVideoActivity : AppCompatActivity() {

    private var embeddedUrl: String? = null
    private var webView: WebView? = null
    private var progressBar: SpinKitView? = null
    private var decorView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_full_video)

        val intent = intent
        embeddedUrl = intent.getStringExtra("embeddedUrl")

        val frameVideo =
            "<iframe width=\"100%\" height=\"100%\" src=\"$embeddedUrl\" frameborder=\"0\" allowfullscreen></iframe>"

        decorView = window.decorView
        decorView?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN)
        decorView?.setBackgroundColor(Color.BLACK)
        webView = findViewById(R.id.embed_video_web_view)
        progressBar = findViewById<SpinKitView>(R.id.pgb_webview)
        webView?.setBackgroundColor(Color.TRANSPARENT)

        webView?.getSettings()?.javaScriptEnabled = true
        webView?.getSettings()?.javaScriptCanOpenWindowsAutomatically = true
        webView?.getSettings()?.setSupportMultipleWindows(true)
        webView?.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                if (progressBar?.getVisibility() == View.VISIBLE) {
                    progressBar?.setVisibility(View.GONE)
                }
                super.onPageFinished(view, url)
            }
        })

        webView?.setWebChromeClient(WebChromeClient())
        webView?.getSettings()?.domStorageEnabled = true
        webView?.loadDataWithBaseURL(
            "https://absolutelycold.github.io", frameVideo,
            "text/html", null, null
        )

    }

    override fun onPostResume() {
        decorView?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN)
        super.onPostResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        decorView?.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("embedded_url", embeddedUrl)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out)
    }
}
