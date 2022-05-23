package com.cryptoemergency.app

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast

class SplashActivity : AppCompatActivity() {
    var counter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkConnect()
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun timerCheckConnection(context: Context){
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counter++
            }

            override fun onFinish() {
                if(isOnline(context)){
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    checkConnect()
                }
            }
        }.start()
    }

    private fun checkConnect(){
        if(isOnline(this)) {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this,resources.getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
            timerCheckConnection(this)
        }
    }

}