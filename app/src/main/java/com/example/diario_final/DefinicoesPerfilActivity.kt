package com.example.diario_final

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

class DefinicoesPerfilActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicoes_perfil)

        val btnBackDefinicoes = findViewById<ImageButton>(R.id.imageButtonBackDefinicoes)
        btnBackDefinicoes.setOnClickListener{
            val intent = Intent(this,PerfilActivity::class.java)
            startActivity(intent)

        }

        val btnsair = findViewById<Button>(R.id.buttonSair)

        btnsair.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}