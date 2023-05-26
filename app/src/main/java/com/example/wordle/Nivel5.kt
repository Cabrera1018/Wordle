package com.example.wordle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Nivel5 : AppCompatActivity() {

    val tamañoPalabra = 5
    val intentos = 6

    var letras: MutableList<MutableList<Button>> = mutableListOf()
    lateinit var borrar: Button
    lateinit var enter: Button
    lateinit var correct: TextView
    lateinit var palabraAleatoria: String
    lateinit var palabrasDe5Letras: List<String>
    lateinit var letrasTeclado: MutableList<Button>

    // fila y columna actual
    var filaActual = 0
    var columnaActual = 0

    // map de identificadores de recursos a caracteres para el teclado
    var keyMap: MutableMap<Button, String> = mutableMapOf()

    var abecedario = mutableSetOf(
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'ñ',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y'
    )

    // candidate characters for each of the 5 positions of the word
    var candidates = mutableListOf<MutableSet<Char>>(
        abecedario.toMutableSet(), abecedario.toMutableSet(),
        abecedario.toMutableSet(), abecedario.toMutableSet(), abecedario.toMutableSet()
    )

    // the chars which are definitely not in the word because of a previous guess
    var excluded = mutableSetOf<Char>()

    var included = mutableSetOf<Char>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivel5)

        val menu: ImageView = findViewById(R.id.home)
        letrasTeclado = mutableListOf()
        menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        test()
        initialise_widgets()
    }

    fun test() {
        val inputStream = resources.openRawResource(R.raw.words)
        val palabras = inputStream.bufferedReader().readLines()

        palabrasDe5Letras = palabras.filter {
            it.length == tamañoPalabra
        }
        palabraAleatoria = palabrasDe5Letras.random()

        val mensaje = "Palabra aleatoria de 5 letras: $palabraAleatoria"
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    fun initialise_widgets() {
        val letter_buttons_ids = listOf(
            listOf(R.id.r00, R.id.r01, R.id.r02, R.id.r03, R.id.r04),
            listOf(R.id.r10, R.id.r11, R.id.r12, R.id.r13, R.id.r14),
            listOf(R.id.r20, R.id.r21, R.id.r22, R.id.r23, R.id.r24),
            listOf(R.id.r30, R.id.r31, R.id.r32, R.id.r33, R.id.r34),
            listOf(R.id.r40, R.id.r41, R.id.r42, R.id.r43, R.id.r44),
            listOf(R.id.r50, R.id.r51, R.id.r52, R.id.r53, R.id.r54)
        )

        // lista de botones que muestran las letras
        for (row in 0..5) {
            val list = mutableListOf<Button>()
            for (col in 0..4) {
                list.add(findViewById(letter_buttons_ids[row][col]))
            }
            letras.add(list)
        }

        val idLetras = listOf(
            R.id.a, R.id.b, R.id.c, R.id.d, R.id.e, R.id.f, R.id.g, R.id.h, R.id.i,
            R.id.j, R.id.k, R.id.l, R.id.m, R.id.n, R.id.o, R.id.p, R.id.r, R.id.q, R.id.ñ,
            R.id.s, R.id.t, R.id.u, R.id.v, R.id.w, R.id.x, R.id.y, R.id.z
        )

        // listener para todas las teclas
        val listener = teclas()

        // Iniciar las teclas para el map
        for (k in idLetras) {
            val b = findViewById<Button>(k)
            b.setOnClickListener(listener)
            keyMap.put(b, b.text.toString())
        }

        // configurar el botón para borrar y el listener
        borrar = findViewById<Button>(R.id.del)
        borrar.setOnClickListener(BorrarListener())

        // configurar el botón enter y el listener
        enter = findViewById<Button>(R.id.enter)
        enter.setOnClickListener(EnterListener())


        correct = findViewById<TextView>(R.id.answer)
    }


    // Listener para el teclado
    inner class teclas : View.OnClickListener {
        override fun onClick(v: View?) {
            if (columnaActual < tamañoPalabra && filaActual < intentos) {
                letras[filaActual][columnaActual].text = keyMap[(v as Button)]
                ++columnaActual
            }
            // Agregar esta línea para almacenar el botón en letrasTeclado
            letrasTeclado.add(v as Button)
        }
    }

    fun checkGuess(): Boolean {

        // Obtener la suposición del usuario
        var guess = ""
        for (b in letras[filaActual])
            guess += b.text


        // Verifica si la suposición es correcta
        val found = palabrasDe5Letras.any { word ->
            word.equals(guess, ignoreCase = true)
        }

        if (!found) {
            correct.text = "No existe"
            return false
        }

        // Suposición correcta: cambia el color de las letras
        if (guess.uppercase() == palabraAleatoria.uppercase()) {
            Toast.makeText(this, "Correcto", Toast.LENGTH_SHORT).show()
            // Cambia el color de las letras a verde
            for (i in 0 until tamañoPalabra)
                letras[filaActual][i].setBackgroundResource(R.drawable.c_verde)
            filaActual = intentos
        } else {
            for (i in 0 until tamañoPalabra) {
                val letraIngresada = letras[filaActual][i].text.toString().uppercase()
                val letraSecreta = palabraAleatoria[i].toString().uppercase()

                for (button in letrasTeclado) {


                    if (letraIngresada == letraSecreta) {
                        letras[filaActual][i].setBackgroundResource(R.drawable.c_verde)
                        button.setBackgroundResource(R.drawable.c_verde)
                        candidates[i] = mutableSetOf(letraSecreta[0])
                        included += letraIngresada[0].lowercaseChar()
                    } else if (letraIngresada in palabraAleatoria.uppercase()) {
                        letras[filaActual][i].setBackgroundResource(R.drawable.c_amarillo)
                        button.setBackgroundResource(R.drawable.c_amarillo)
                        candidates[i] -= mutableSetOf(letraIngresada[0].lowercaseChar())
                        included += letraIngresada[0].lowercaseChar()
                    } else {
                        letras[filaActual][i].setBackgroundResource(R.drawable.c_gris)
                        button.setBackgroundResource(R.drawable.c_gris)
                        excluded += letraIngresada[0].lowercaseChar()
                    }
                }
            }

            excluded -= included
            // Remueve las letras excluidas de los candidatos
            for (i in 0 until tamañoPalabra)
                candidates[i] -= excluded
        }

        return true
    }

      /*private fun actualizarColoresTeclado() {
        for (button in letrasTeclado) {
            val letra = button.text.toString().uppercase()
            val char = letra[0]
            if (char in included) {
                button.setBackgroundResource(R.drawable.c_verde)
            } else if (char in excluded) {
                button.setBackgroundResource(R.drawable.c_gris)
            } else {
                button.setBackgroundResource(R.drawable.c_amarillo)
            }
        }
    }*/


    // Listener para el enter
    inner class EnterListener : View.OnClickListener {
        override fun onClick(v: View?) {
            if (filaActual < intentos && columnaActual == tamañoPalabra) {
                val valid_submission = checkGuess()
                if (valid_submission) {
                    ++filaActual
                    columnaActual = 0  // Cambia de columna para otro intento
                }
            }

            // muestra la palabra correcta
            if (filaActual == intentos) {
                correct.text = palabraAleatoria.uppercase()
            }
        }
    }

    // Para borrar
    inner class BorrarListener : View.OnClickListener {
        override fun onClick(v: View?) {
            correct.text = ""
            if (columnaActual > 0) {
                --columnaActual
                letras[filaActual][columnaActual].text = ""
            }
        }
    }
}