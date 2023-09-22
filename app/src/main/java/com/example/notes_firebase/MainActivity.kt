package com.example.notes_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lottieView: LottieAnimationView = findViewById(R.id.img)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

//        lottieView.setAnimation(R.raw.notes)
//
//        lottieView. playAnimation()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {

            startActivity(Intent(this,LoginActivity::class.java))
            finish()

        },2500)

    }
}