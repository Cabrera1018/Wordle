package com.example.wordle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class Victoria4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_victoria4)

        //Redirigir al menu
        val menu: ImageView = findViewById(R.id.home)
        menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        //Jugar de nuevo
        val deNuevo: Button = findViewById(R.id.bVolverjugar)
        deNuevo.setOnClickListener {
            val intent = Intent(this, Nivel4::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Borra todas las actividades anteriores en la pila
            startActivity(intent)
            finish() // Opcional: cierra la actividad actual si ya no es necesaria
        }

        //Probar otro nivel
        val otroNivel: Button = findViewById(R.id.bProbarotro)
        otroNivel.setOnClickListener {
            val intent = Intent(this, Niveles::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
        }

        val palabraCorrecta = intent.getStringExtra("palabra_correcta")

        if (palabraCorrecta != null) {
            for (i in palabraCorrecta.indices) {
                val letra = palabraCorrecta[i]
                val ids = resources.getIdentifier("tvLetra${i + 1}", "id", packageName)
                val messi: Button = findViewById(ids)
                messi.text = letra.toString()
            }
        } else {
            // Por si acaso
        }

        // Obtener la lista de intentos del intent
        val intentos = intent.getStringArrayListExtra("palabras_intentadas")

        if (intentos != null) {
            for (i in intentos.indices) {
                val palabraIntento = intentos[i]
                val letrasIntento = palabraIntento.toCharArray()

                for (j in letrasIntento.indices) {
                    val letra = letrasIntento[j]
                    val buttonId = resources.getIdentifier("m${i + 1}${j + 1}", "id", packageName)
                    val button: Button = findViewById(buttonId)
                    button.text = letra.toString()
                }
            }
        } else{

        }

        val coloresBotones = intent.getIntegerArrayListExtra("colores_botones")
        if (coloresBotones != null) {
            for (i in coloresBotones.indices) {
                val colorButtonId = coloresBotones[i]
                val row = (i / 4) + 1
                val col = (i % 4) + 1
                val buttonId = resources.getIdentifier("m$row$col", "id", packageName)
                val button: Button = findViewById(buttonId)
                button.setBackgroundResource(colorButtonId)
            }
        }

    }
}