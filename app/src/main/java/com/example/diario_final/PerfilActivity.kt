package com.example.diario_final

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class PerfilActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    val storageRef = FirebaseStorage.getInstance().reference
    val storageReference = Firebase.storage.reference
    val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnPerfilBack = findViewById<ImageButton>(R.id.imageButtonBackPerfil)
        btnPerfilBack.setOnClickListener {
            val intent = Intent(this, PrivadoActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnPerfilDefinicoes = findViewById<ImageButton>(R.id.imageButtonDefinicoes)
        btnPerfilDefinicoes.setOnClickListener {
            val intent = Intent(this, DefinicoesPerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        val calendar = findViewById<CalendarView>(R.id.calendarViewDiarios)

        calendar.setOnDateChangeListener(CalendarView.OnDateChangeListener { view, year, month, dayOfMonth ->
            val date = (dayOfMonth.toString() + "-" + (month + 1) + "-" + year)
            println(date)
            abrirpaginaweb(date)


        })



        val username = findViewById<TextView>(R.id.textViewNomeUtilizador)
        val imageview = findViewById<ImageView>(R.id.imageViewFotoPerfil)


        user?.let {
            // Name, email address, and profile photo Url
            username.text = it.displayName

        }

        //val ref = storageRef.child("Images/${user?.uid}.jpg")
        //imageview.setImageURI(ref as Uri?)

       /* Glide.with(this)
            .load(storageReference.child("Images/${user?.uid}.jpg").toString())
            .into(imageview)*/




    }

    fun abrirpaginaweb(date : String){

        user?.let {
            val docRef = db.collection(date).document(it.uid)
            docRef.get().addOnSuccessListener { document ->
                if (document.data?.get("videolink") != null){
                    println(document.data?.get("videolink"))

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(document.data?.get("videolink") as String?)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"Não existe vídeo neste dia" , Toast.LENGTH_SHORT).show()
                }

            }

        }

    }


}