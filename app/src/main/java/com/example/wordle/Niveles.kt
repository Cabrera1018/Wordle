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
        val atras: ImageView = findViewById(R.id.home)
        val nivel4: Button = findViewById(R.id.letras4)
        val nivel5: Button = findViewById(R.id.letras5)
        val nivel6: Button = findViewById(R.id.letras6)

        atras.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        nivel4.setOnClickListener {
            val intent = Intent(this, Nivel4::class.java)
            startActivity(intent)
        }

        nivel5.setOnClickListener {
            val intent = Intent(this, Nivel5::class.java)
            startActivity(intent)
        }

        nivel6.setOnClickListener {
            val intent = Intent(this, Nivel6::class.java)
            startActivity(intent)
        }
    }
}