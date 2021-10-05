package com.example.targetsdk30

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val createButton = findViewById<Button>(R.id.button1)
        val updateButton = findViewById<Button>(R.id.button2)
        val readButton = findViewById<Button>(R.id.button3)
        createButton.setOnClickListener(this)
        updateButton.setOnClickListener(this)
        readButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.button1 -> {
                val intent  = Intent(this,CreateFileActivity::class.java)
                startActivity(intent)
            }
            R.id.button2 -> print("x == 2")
            R.id.button3 -> print("x == 2")
        }
    }
}