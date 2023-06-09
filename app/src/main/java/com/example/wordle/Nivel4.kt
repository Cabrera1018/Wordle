package com.example.wordle

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.text.Normalizer
import java.util.Locale
import java.util.regex.Pattern


class Nivel4 : AppCompatActivity() {

    val sizePalabra = 4
    val intentos = 6
    var letras: MutableList<MutableList<Button>> = mutableListOf()
    lateinit var borrar: Button
    lateinit var enter: Button
    lateinit var palabraAleatoria: String
    lateinit var palabras: List<String>
    lateinit var letrasTeclado: MutableList<Button>
    var coloresBotones: MutableList<Int> = mutableListOf()
    var palabrasIntentadas: MutableList<String> = mutableListOf()

    // fila y columna actual
    var filaActual = 0
    var columnaActual = 0

    // map de identificadores de botones a caracteres del teclado
    var keyMap: MutableMap<Button, String> = mutableMapOf()

    //Representan las letras del teclado
    var abecedario = mutableSetOf(
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'ñ',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    )

    // Letras habilitadas para las 4 posiciones (todas las letras letrasDisponibles)
    var letrasDisponibles = mutableListOf(
        abecedario.toMutableSet(), abecedario.toMutableSet(),abecedario.toMutableSet(),
        abecedario.toMutableSet()
    )

    // Letras que no están en la palabra
    var noEstan = mutableSetOf<Char>()
    // Letras que si están en la palabra
    var siEstan = mutableSetOf<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nivel4)

        //Inicia la lista del teclado
        letrasTeclado = mutableListOf()

        //Llama a la funcion para obtener la palabra y la de los Cuadros
        ObtenerPalabra()
        Cuadros()

        //Redirigir al menu
        val menu: ImageView = findViewById(R.id.home)
        menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        //Hacia atras
        val atras: ImageView = findViewById(R.id.back)
        atras.setOnClickListener {
            onBackPressed()
        }
    }

    fun ObtenerPalabra() {
        val url = "https://api.datamuse.com/words?sp=????&v=es&max=4000"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // Por si hay error
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                //Se obtiene la respuesta de la API en forma de String
                val responseData = response.body?.string()

                // Procesar la respuesta de la API
                val palabras = procesarRespuesta(responseData)

                // Seleccionar una palabra aleatoria de la lista
                palabraAleatoria = palabras.random()

                // Eliminar acentos de la palabra aleatoria
                val palabraSinAcento = unaccent(palabraAleatoria)

                // Visualizar el TextView
                runOnUiThread {
                    val textView = findViewById<TextView>(R.id.wordle)
                    textView.text = palabraSinAcento
                }
            }
        })
    }

    private fun procesarRespuesta(responseData: String?): List<String> {

        // Guarda todas las palabras extraídas de la respuesta de la API
        palabras  = mutableListOf<String>()

        try {
            val jsonArray = JSONArray(responseData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val palabra = jsonObject.getString("word")
                val palabraSinAcento = unaccent(palabra)
                (palabras as MutableList<String>).add(palabraSinAcento)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return palabras
    }
    private fun unaccent(word: String): String {
        val normalized = Normalizer.normalize(word, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(normalized).replaceAll("").lowercase(Locale.getDefault())
    }
    fun Cuadros() {
        val idCuadros = listOf(
            listOf(R.id.r00, R.id.r01, R.id.r02, R.id.r03),
            listOf(R.id.r10, R.id.r11, R.id.r12, R.id.r13),
            listOf(R.id.r20, R.id.r21, R.id.r22, R.id.r23),
            listOf(R.id.r30, R.id.r31, R.id.r32, R.id.r33),
            listOf(R.id.r40, R.id.r41, R.id.r42, R.id.r43),
            listOf(R.id.r50, R.id.r51, R.id.r52, R.id.r53)
        )

        // lista de botones que muestran las letras
        for (fila in 0..5) {
            val listaB = mutableListOf<Button>()
            for (columna in 0..3) {
                listaB.add(findViewById(idCuadros[fila][columna]))
            }
            letras.add(listaB)
        }

        val idLetras = listOf(
            R.id.a, R.id.b, R.id.c, R.id.d, R.id.e, R.id.f, R.id.g, R.id.h, R.id.i,
            R.id.j, R.id.k, R.id.l, R.id.m, R.id.n, R.id.o, R.id.p, R.id.r, R.id.q, R.id.ñ,
            R.id.s, R.id.t, R.id.u, R.id.v, R.id.w, R.id.x, R.id.y, R.id.z
        )

        // listener para todas las teclas
        val listenerT = teclas()

        // Iniciar las teclas para el map
        for (k in idLetras) {
            val b = findViewById<Button>(k)
            b.setOnClickListener(listenerT)
            keyMap.put(b, b.text.toString())
        }

        // configurar el boton para borrar y el listener
        borrar = findViewById(R.id.del)
        borrar.setOnClickListener(BorrarListener())

        // configurar el boton enter y el listener
        enter = findViewById(R.id.enter)
        enter.setOnClickListener(EnterListener())
    }


    // Listener para el teclado
    inner class teclas : View.OnClickListener {
        override fun onClick(v: View?) {
            if (columnaActual < sizePalabra && filaActual < intentos) {
                letras[filaActual][columnaActual].text = keyMap[(v as Button)]
                ++columnaActual
            }
            // Esta linea para almacenar el boton en letrasTeclado
            letrasTeclado.add(v as Button)
        }
    }

    fun RevisarIntento(): Boolean {
        // Obtener la suposicion del usuario
        var intento = ""
        for (b in letras[filaActual])
            intento += b.text


        // Verifica si la suposicion es correcta
        val encuentra = palabras.any { word ->
            word.equals(intento, ignoreCase = true)
        }

        if (!encuentra) {
            Toast.makeText(applicationContext, "Palabra no encontrada", Toast.LENGTH_SHORT).show()
            return false
        }

        palabrasIntentadas.add(intento)
        // Opcion correcta: cambia el color de las letras
        if (intento.uppercase() == palabraAleatoria.uppercase()) {
            val intent = Intent(this@Nivel4, Victoria4::class.java)
            intent.putExtra("palabra_correcta", palabraAleatoria)
            intent.putStringArrayListExtra("palabras_intentadas", ArrayList(palabrasIntentadas))
            intent.putIntegerArrayListExtra("colores_botones", ArrayList(coloresBotones)) // Agregar la lista de identificadores de recursos drawable
            startActivity(intent)


            // Cambia el color de las letras a verde
            for (i in 0 until sizePalabra)
                letras[filaActual][i].setBackgroundResource(R.drawable.c_verde)
            filaActual = intentos
        } else {
            for (i in 0 until sizePalabra) {
                val letraIngresada = letras[filaActual][i].text.toString().uppercase()
                val letraSecreta = palabraAleatoria[i].toString().uppercase()

                //Si la letra ingresada está en el lugar correcto
                if (letraIngresada == letraSecreta) {
                    letras[filaActual][i].setBackgroundResource(R.drawable.c_verde)
                    letrasDisponibles[i] = mutableSetOf(letraSecreta[0])
                    siEstan += letraIngresada[0].lowercaseChar()

                    //Si la letra ingresada no está en el lugar correcto
                } else if (letraIngresada in palabraAleatoria.uppercase()) {
                    letras[filaActual][i].setBackgroundResource(R.drawable.c_amarillo)
                    letrasDisponibles[i] -= mutableSetOf(letraIngresada[0].lowercaseChar())
                    siEstan += letraIngresada[0].lowercaseChar()

                    //Si la letra ingresada no está en la palabra
                } else {
                    letras[filaActual][i].setBackgroundResource(R.drawable.c_gris)
                    noEstan += letraIngresada[0].lowercaseChar()
                }
                val colorButtonId = when {
                    intento.uppercase() == palabraAleatoria.uppercase()-> R.drawable.c_verde
                    letraIngresada == letraSecreta -> R.drawable.c_verde
                    letraIngresada in palabraAleatoria.uppercase() -> R.drawable.c_amarillo
                    else -> R.drawable.c_gris
                }
                coloresBotones.add(colorButtonId)
            }

            noEstan -= siEstan
            // Remueve las letras excluidas
            for (i in 0 until sizePalabra)
                letrasDisponibles[i] -= noEstan
        }
        return true
    }

    // Para el enter
    inner class EnterListener : View.OnClickListener {
        override fun onClick(v: View?) {
            if (filaActual < intentos && columnaActual == sizePalabra) {
                val chequear = RevisarIntento()
                if (chequear) {
                    ++filaActual
                    columnaActual = 0  // Cambia de columna para otro intento
                }
            }

            // muestra la palabra correcta cuando se llega al numero maximo de intentos
            // muestra la palabra correcta cuando se llega al numero maximo de intentos
            if (filaActual == intentos) {
                val perdiste = Intent(this@Nivel4, Derrota4::class.java)
                perdiste.putStringArrayListExtra("palabras_intentadas", ArrayList(palabrasIntentadas))
                perdiste.putIntegerArrayListExtra("colores_botones", ArrayList(coloresBotones)) // Agregar la lista de identificadores de recursos drawable
                startActivity(perdiste)
            }
        }
    }

    // Para borrar
    inner class BorrarListener : View.OnClickListener {
        override fun onClick(v: View?) {

            if (columnaActual > 0) {
                --columnaActual
                letras[filaActual][columnaActual].text = ""
            }
        }
    }
}