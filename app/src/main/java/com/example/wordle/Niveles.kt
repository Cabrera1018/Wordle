package com.example.wordle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class Niveles : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_niveles)
        val menu: ImageView = findViewById(R.id.home)
        val nivel5: Button = findViewById(R.id.letras5)

        menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        nivel5.setOnClickListener {
            val intent = Intent(this, Nivel5::class.java)
            startActivity(intent)
        }
    }
}