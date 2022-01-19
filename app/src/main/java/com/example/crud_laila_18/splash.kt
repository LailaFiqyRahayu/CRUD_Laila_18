package com.example.crud_laila_18

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        supportActionBar?.hide()
        Handler().postDelayed({
            val intent = Intent (this@splash, Home::class.java)
            startActivity(intent)
        }, 3000)
    }
}