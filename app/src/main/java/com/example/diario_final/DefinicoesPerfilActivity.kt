package com.example.diario_final

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class DefinicoesPerfilActivity : AppCompatActivity() {

    private val pickImage = 100
    private var imageUri: Uri? = null
    private val user = Firebase.auth.currentUser

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_definicoes_perfil)

        val btnBackDefinicoes = findViewById<ImageButton>(R.id.imageButtonBackDefinicoes)
        btnBackDefinicoes.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        val btnsair = findViewById<Button>(R.id.buttonSair)
        btnsair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnfoto = findViewById<Button>(R.id.buttonfoto)
        btnfoto.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        val btnGuardar = findViewById<Button>(R.id.buttonGuardar)
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val pass = findViewById<EditText>(R.id.editTextTextPassword)
        val nomeu = findViewById<EditText>(R.id.editTextTextUser)


        btnGuardar.setOnClickListener {

            /*if (email.text.isEmpty() && pass.text.isEmpty() && nomeu.text.isEmpty()) {
                Toast.makeText(this, "Preencha os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }*/
            val firebaseStorage = FirebaseStorage.getInstance()
            val imageReference = firebaseStorage.reference.child("Images/${user?.uid}.jpg")
            imageUri?.let { it1 -> imageReference.putFile(it1) }


            /* user!!.updateEmail(email.text.toString())
                 .addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         Log.d(TAG, "User email address updated.")
                     }
                 }
             user!!.updatePassword(pass.text.toString())
                 .addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         Log.d(TAG, "User password updated.")
                     }
                 }*/

            val profileUpdates: UserProfileChangeRequest

            if (nomeu.text.toString().isEmpty()) {
                profileUpdates = userProfileChangeRequest {
                    photoUri = imageUri
                }

            } else {
                profileUpdates = userProfileChangeRequest {
                    displayName = nomeu.text.toString()
                    photoUri = imageUri
                }

            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "User name updated.")
                    }
                }
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
        }
    }
}