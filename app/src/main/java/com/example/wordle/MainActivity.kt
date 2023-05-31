package com.example.wordle
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.io.File


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

        val btnSalir: Button = findViewById(R.id.bSalir)
        btnSalir.setOnClickListener {
            // Finalizar la actividad actual y salir de la aplicación
            finish()
            System.exit(0)
        }

    }
}