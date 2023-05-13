package com.example.diario_final

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.example.diario_final.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldValue.serverTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


import com.google.firebase.storage.StorageException
import com.google.firebase.storage.UploadTask

import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2
import com.google.firebase.storage.ktx.component3
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File
import java.lang.reflect.Field
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DiarioActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var btnConf: Button
    val storage = Firebase.storage
    private lateinit var database : DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var uri : Uri
    val user = Firebase.auth.currentUser
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference : DatabaseReference
    val db = Firebase.firestore

    //private lateinit var mStorage : storage.reference


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diario)
        val video : Int = 0

        videoView = findViewById(R.id.videoViewDiario)

        btnConf = findViewById(R.id.buttonConfirmar)


        val bundle = getIntent().getExtras()

        if (bundle!=null){

           uri = Uri.parse(bundle.getString("vid"))
            videoView.setVideoURI(uri)
            videoView.start()
        }

        btnConf.setOnClickListener {
            if (uri == null){
                Toast.makeText(this,"Erro no video",Toast.LENGTH_SHORT).show()
            }else{
                uploadVideoFirebase()
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadVideoFirebase() {
        if (uri != null) {
// save the selected video in Firebase storage

            println(uri)

            val reference = FirebaseStorage.getInstance().getReference("videos/video_${System.currentTimeMillis()}")

            println(reference)

            reference.putFile(uri).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
// get the link of video
                val downloadUri = uriTask.result.toString()
                val reference1 = FirebaseDatabase.getInstance().getReference("Video")
                val map: MutableMap<String, String> = HashMap()
                map["videolink"] = downloadUri
                reference1.child("${System.currentTimeMillis()}").setValue(map)

                //adicionar video à bd com id do user
                var localDate = LocalDate.now()
                val DateTime = DateTimeFormatter.ofPattern("dd-M-yyyy")
                val formated = localDate.format(DateTime)
                user?.let {
                    db.collection(it.uid).document(formated.toString()).set(map)
                }
                // Video uploaded successfully
                // Dismiss dialog
                Toast.makeText(this, "Video Uploaded!!", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { e ->
                // Error, Image not uploaded
                Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
            }.addOnProgressListener { taskSnapshot ->
                // Progress Listener for loading
            // percentage on the dialog box
            // show the progress bar
                //val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            }
        }
    }
}