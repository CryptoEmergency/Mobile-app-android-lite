package com.cryptoemergency.app

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Message
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.WebViewTransport
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging


class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var fbToken: String
    var counter = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFireBaseToken()
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window?.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val url=resources.getString(R.string.url)
        webView = findViewById(R.id.webview)


        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true


        object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counter++
            }

            override fun onFinish() {
                 findViewById<ConstraintLayout>(R.id.splash_screen).apply {
                    visibility = View.GONE}
            }
        }.start()




        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView,
                dialog: Boolean,
                userGesture: Boolean,
                resultMsg: Message
            ): Boolean {

                val newWebView = WebView(this@MainActivity)
                view.addView(newWebView)
                val transport = resultMsg.obj as WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()

                newWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        val browserIntent = Intent(Intent.ACTION_VIEW)
                        browserIntent.data = Uri.parse(url)
                        startActivity(browserIntent)
                        return true
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                
                findViewById<ConstraintLayout>(R.id.splash_screen).apply {
                    visibility = View.GONE
                }
            }
                }
                return true


            }
        }

       
        val settings = webView.settings;

    
        settings.javaScriptEnabled = true

        
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.setAppCachePath(cacheDir.path)


        
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false


        


        
        settings.blockNetworkImage = false
        
        settings.loadsImagesAutomatically = true


       
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.safeBrowsingEnabled = true  // api 26
        }
        settings.pluginState = WebSettings.PluginState.ON
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.mediaPlaybackRequiresUserGesture = false
        }


        
        settings.domStorageEnabled = true
        settings.setSupportMultipleWindows(true)
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        settings.setGeolocationEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.allowUniversalAccessFromFileURLs = true
        }


        settings.allowFileAccess = true

        
        webView.fitsSystemWindows = true

        /* if SDK version is greater of 19 then activate hardware acceleration
        otherwise activate software acceleration  */

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView.loadUrl(url)



        
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }




   
    companion object {

        private const val TAG = "MainActivity"
    }
    @SuppressLint("HardwareIds")
    private fun getFireBaseToken(){
        //Firebase notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW
                )
            )
        }
        
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            
            val token = task.result

            
            val msg = getString(R.string.msg_token_fmt, token)
            

            Log.d(TAG, msg)
            if (token != null) {
                Log.d("фб", token)
                fbToken = token
                webView.settings.userAgentString = "User $fbToken"
                Log.d("какой там юзер агент", webView.settings.userAgentString)
            }

        })
    }

}