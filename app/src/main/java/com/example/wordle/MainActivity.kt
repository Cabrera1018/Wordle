package com.example.wordle
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)


        val backgroundView = findViewById<View>(R.id.backgroundView)
        val colorFrom = resources.getColor(R.color.fondo)
        val colorTo = resources.getColor(R.color.gris)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 3000 // Duración de la animación en milisegundos
        colorAnimation.repeatCount = Animation.INFINITE // Repetir la animación infinitamente
        colorAnimation.repeatMode = ValueAnimator.REVERSE // Invertir la animación al repetirla
        colorAnimation.interpolator = LinearInterpolator() // Interpolador lineal para una transición suave
        colorAnimation.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            backgroundView.setBackgroundColor(color)
        }
        colorAnimation.start()

        val tWordle = findViewById<TextView>(R.id.tWordle)
        val texto = tWordle.text.toString()

        val spannableBuilder = SpannableStringBuilder(texto)

        // Índices de las letras "W" y "E"
        val indiceW = texto.indexOf("W")
        val indiceE = texto.indexOf("E")

        // Colores para las letras "W" y "E"
        val colorW = ContextCompat.getColor(this, R.color.fVerde)
        val colorE = ContextCompat.getColor(this, R.color.fAmarillo)

        // Aplicar el color a la letra "W"
        val spanW = ForegroundColorSpan(colorW)
        spannableBuilder.setSpan(spanW, indiceW, indiceW + 1, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        // Aplicar el color a la letra "E"
        val spanE = ForegroundColorSpan(colorE)
        spannableBuilder.setSpan(spanE, indiceE, indiceE + 1, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)

        tWordle.text = spannableBuilder

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