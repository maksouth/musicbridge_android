package com.yeket.music.bridge.ui.views.privacy_policy

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)

        webView.loadUrl("https://www.freeprivacypolicy.com/privacy/view/ece3203c070f0a2587f843234cc494af")
    }
}
