package com.example.diario_final

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.VideoView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.lang.Exception

class RegistarActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registar)
        auth = Firebase.auth


        val btnReg = findViewById<Button>(R.id.buttonRegistar)
        val btnRegBack = findViewById<ImageButton>(R.id.imageButtonBackRegistar)


        btnRegBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnReg.setOnClickListener {
            createAccount()
        }

    }

    private fun createAccount() {

        val email = findViewById<EditText>(R.id.editTextTextEmailAddressRegistar).text.toString()
        val passe = findViewById<EditText>(R.id.editTextTextPasswordRegistar).text.toString()
        val username = findViewById<EditText>(R.id.editTextTextUsername).text.toString()


        if (email.isEmpty() || passe.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Campo Vazio", Toast.LENGTH_SHORT).show()
            return
        } else {

            auth.createUserWithEmailAndPassword(email, passe)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                        val user = Firebase.auth.currentUser
                        val profileUpdates = userProfileChangeRequest {
                            displayName = username
                        }

                        user!!.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User name updated.")
                                }
                            }

                        Toast.makeText(
                            baseContext, "Registado com Sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        Toast.makeText(
                            baseContext, "Falha no Registo.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
        }


    }

}