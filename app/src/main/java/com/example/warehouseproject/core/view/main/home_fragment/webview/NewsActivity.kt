package com.example.warehouseproject.core.view.main.home_fragment.webview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.warehouseproject.R
import com.example.warehouseproject.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        val wv = binding.webView

        wv.webViewClient = WebViewClient()
        wv.loadUrl("https://developer.android.com/")
        wv.settings.javaScriptEnabled = true
        wv.settings.setSupportZoom(true)

    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}