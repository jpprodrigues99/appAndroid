package com.example.diario_final

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.get
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PerfilActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnPerfilBack = findViewById<ImageButton>(R.id.imageButtonBackPerfil)
        btnPerfilBack.setOnClickListener{
            val intent = Intent(this,PrivadoActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnPerfilDefinicoes = findViewById<ImageButton>(R.id.imageButtonDefinicoes)
        btnPerfilDefinicoes.setOnClickListener{
            val intent = Intent(this,DefinicoesPerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        val calendar = findViewById<CalendarView>(R.id.calendarViewDiarios)

        calendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            val Date = (dayOfMonth.toString() + "-"+ (month + 1) + "-" + year)
            println(Date)
        })

        val username = findViewById<TextView>(R.id.textViewNomeUtilizador)
        user?.let {
            // Name, email address, and profile photo Url
            username.text = it.displayName

        }

    }
}