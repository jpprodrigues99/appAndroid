package com.example.diario_final


import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class DiarioActivity : AppCompatActivity() {
    private lateinit var uri: Uri
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diario)

        val videoView = findViewById<VideoView>(R.id.videoViewDiario)
        val btnConf = findViewById<Button>(R.id.buttonConfirmar)
        val btnElim = findViewById<Button>(R.id.buttonEliminar)

        val bundle = getIntent().getExtras()

        if (bundle != null) {

            uri = Uri.parse(bundle.getString("vid"))
            videoView.setVideoURI(uri)
            videoView.start()
        }

        btnConf.setOnClickListener {
            if (uri == null) {
                Toast.makeText(this, "Erro no video", Toast.LENGTH_SHORT).show()
            } else {
                uploadVideoFirebase()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        btnElim.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadVideoFirebase() {
        if (uri != null) {
            // save the selected video in Firebase storage

            val reference = FirebaseStorage.getInstance()
                .getReference("videos/video_${System.currentTimeMillis()}")


            reference.putFile(uri).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                // get the link of video
                val downloadUri = uriTask.result.toString()

                var localDate = LocalDate.now()
                val DateTime = DateTimeFormatter.ofPattern("dd-M-yyyy")
                val formated = localDate.format(DateTime)

                val localhora = LocalTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val hora = formatter.format(localhora)

                user?.let {
                    val map = hashMapOf(
                        "videolink" to downloadUri,
                        "username" to it.displayName.toString(),
                        "data" to formated.toString(),
                        "hora" to hora
                    )
                    db.collection(formated.toString()).document(it.uid).set(map)
                }
                Toast.makeText(this, "Video Uploaded!!", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { e ->
                // Error, Image not uploaded
                Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}