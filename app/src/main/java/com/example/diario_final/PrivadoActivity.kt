package com.example.diario_final

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PrivadoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    val user = Firebase.auth.currentUser

    val RESQUEST_VIDEO_CAPTURE = 100
    val db = Firebase.firestore

    @RequiresApi(Build.VERSION_CODES.O)
    var localDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val DateTime = DateTimeFormatter.ofPattern("dd-M-yyyy")
    @RequiresApi(Build.VERSION_CODES.O)
    val formated = localDate.format(DateTime)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privado)


        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewPrivado)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        val btnAmigos = findViewById<Button>(R.id.buttonAmigos)
        btnAmigos.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        addvidpriv()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addvidpriv(){

        user?.let {
            videoView = findViewById(R.id.videoViewPrivado)
            val docRef = db.collection(it.uid).document(formated.toString())
            val editdata = findViewById<TextView>(R.id.textViewData)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        editdata.text = formated.toString()
                        videoView.setVideoPath(document.data?.get("videolink").toString())
                        videoView.start()

                    } else {
                        Log.d(TAG, "No such document")
                        videoView.visibility = android.view.View.INVISIBLE
                        editdata.visibility = android.view.View.INVISIBLE
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                    videoView.visibility = android.view.View.INVISIBLE
                    editdata.visibility = android.view.View.INVISIBLE
                }




        }




    }

    val navListener = BottomNavigationView.OnNavigationItemSelectedListener(){ item->

        when (item.itemId) {
            R.id.addAmigo -> {
                // put your code here
                val intent = Intent(this, AdAmigoActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.add -> {
                // put your code here
                val take = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                try {
                    startActivityForResult(take,RESQUEST_VIDEO_CAPTURE)

                }catch (e: ActivityNotFoundException){
                    Toast.makeText(this,"ERROR" + e.localizedMessage, Toast.LENGTH_SHORT).show()
                }


                return@OnNavigationItemSelectedListener true
            }
            R.id.perfil -> {
                // put your code here
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && data!=null) {
            val videoUri = data.getData()
            val intent = Intent(this,DiarioActivity::class.java)
            intent.putExtra("vid",videoUri.toString())
            startActivity(intent)
            //videoView.setVideoURI(videoUri)
            //videoView.start()

        }
    }
}