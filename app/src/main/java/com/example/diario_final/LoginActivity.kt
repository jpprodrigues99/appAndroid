package com.example.diario_final

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            // No user is signed in

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        val btnIniciar = findViewById<Button>(R.id.buttonIniciar)
        btnIniciar.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnRegistar = findViewById<Button>(R.id.buttonRegistar)
        btnRegistar.setOnClickListener {
            val intent = Intent(this, RegistarActivity::class.java)
            startActivity(intent)
            finish()
        }



        val btnLogin = findViewById<Button>(R.id.buttonIniciar)
        btnLogin.setOnClickListener{
            signIn()
            //val intent = Intent(this, MainActivity::class.java)
            //startActivity(intent)


        }
        }
    }

    private fun signIn(){
        val email = findViewById<EditText>(R.id.editTextTextEmailAddressLogin).text.toString()
        val passe = findViewById<EditText>(R.id.editTextTextPasswordLogin).text.toString()



        if(email.isEmpty() || passe.isEmpty()){
            Toast.makeText(this,"Campo Vazio",Toast.LENGTH_SHORT).show()
            return
        }else{
            auth.signInWithEmailAndPassword(email, passe)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)



                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
        }

    }
}