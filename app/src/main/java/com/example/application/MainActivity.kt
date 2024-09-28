package com.example.application

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listNew = findViewById<ListView>(R.id.list)
        val clientData: EditText = findViewById(R.id.client_data)
        val button = findViewById<Button>(R.id.button)
        val link = findViewById<TextView>(R.id.link)

        link.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val products: MutableList<String> = mutableListOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, products)
        listNew.adapter = adapter

        listNew.setOnItemClickListener { adapterView, view, i, l ->
            val text = listNew.getItemAtPosition(i).toString()
            adapter.remove(text)

            Toast.makeText(this, "Мы удалили: $text", Toast.LENGTH_LONG).show()
        }

        button.setOnClickListener {
            val text = clientData.text.toString().trim()
            if(text != "")
                adapter.insert(text, 0)
        }

    }
}