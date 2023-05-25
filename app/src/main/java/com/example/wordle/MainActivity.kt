package com.example.wordle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.sql.BatchUpdateException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val cJugar: Button = findViewById(R.id.bComojugar)
        val jugar : Button = findViewById(R.id.bJugar)

        cJugar.setOnClickListener {
            val intent = Intent(this, Informacion::class.java)
            startActivity(intent)
        }

        jugar.setOnClickListener {
            val intent = Intent(this, Niveles::class.java)
            startActivity(intent)
        }
    }
}