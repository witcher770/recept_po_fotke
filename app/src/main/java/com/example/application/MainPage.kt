package com.example.application

import android.content.Intent
import android.graphics.Picture
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val takePhoto = findViewById<TextView>(R.id.takePhoto)
        val photoHistory = findViewById<TextView>(R.id.photoHistory)
        val findRecipe = findViewById<TextView>(R.id.findRecipe)
        val settings = findViewById<ImageView>(R.id.settings)

        settings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

    }
}