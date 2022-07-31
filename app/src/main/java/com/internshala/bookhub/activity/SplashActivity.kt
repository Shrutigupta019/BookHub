package com.internshala.bookhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.internshala.bookhub.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        fun openNewActivity(){
            val startAct = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(startAct)
            finish()
        }
       Handler().postDelayed({
           openNewActivity();
       }, 2000)
    }
}