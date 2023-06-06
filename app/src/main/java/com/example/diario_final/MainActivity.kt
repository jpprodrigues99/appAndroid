package com.example.diario_final

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CloudMediaProviderContract.MediaCollectionInfo
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Document
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private var reachedTop = false
    private var reachedBottom = false
    val RESQUEST_VIDEO_CAPTURE = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewAmigos)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        val btnPrivado = findViewById<Button>(R.id.buttonPrivado)
        btnPrivado.setOnClickListener {
            val intent = Intent(this, PrivadoActivity::class.java)
            startActivity(intent)
            finish()
        }
        recyclerView()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun recyclerView() {
        val user = Firebase.auth.currentUser
        user?.let {
            val recyclerview = findViewById<RecyclerView>(R.id.RecyclerViewAmigos)
            recyclerview.layoutManager = LinearLayoutManager(this)
            val data = ArrayList<ItemsViewModel>()
            val db = FirebaseFirestore.getInstance()
            val localDate = LocalDate.now()
            val DateTime = DateTimeFormatter.ofPattern("dd-M-yyyy")
            val formated = localDate.format(DateTime)
            val docRef = db.collection(formated.toString()).document(it.uid)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.data?.get("videolink") != null) {
                        db.collection(formated).orderBy(
                            "hora",
                            com.google.firebase.firestore.Query.Direction.DESCENDING
                        )
                            .get()
                            .addOnSuccessListener { documents ->

                                for (document in documents) {

                                    val item = ItemsViewModel(
                                        document.data.get("videolink").toString(),
                                        document.data.get("username").toString(),
                                        document.data.get("data").toString()
                                    )
                                    data.add(item)
                                }
                                val adapter = RecyclerAdapter(data)
                                recyclerview.adapter = adapter
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Ainda não publicaste o diário de hoje",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }


    val navListener = BottomNavigationView.OnNavigationItemSelectedListener() { item ->

        when (item.itemId) {
            R.id.addAmigo -> {
                // val intent = Intent(this, AdAmigoActivity::class.java)
                // startActivity(intent)
                //return@OnNavigationItemSelectedListener true
            }

            R.id.add -> {
                val take = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

                val camaraPermission = arrayOf(android.Manifest.permission.CAMERA)
                val permission = checkPermissions(camaraPermission)

                if (!permission) {
                    ActivityCompat.requestPermissions(
                        this,
                        camaraPermission,
                        RESQUEST_VIDEO_CAPTURE
                    )
                    return@OnNavigationItemSelectedListener false
                }
                try {
                    startActivityForResult(take, RESQUEST_VIDEO_CAPTURE)

                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "ERROR" + e.localizedMessage, Toast.LENGTH_SHORT).show()
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
        if (requestCode == RESQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && data != null) {
            val videoUri = data.getData()
            println(videoUri?.getPath())
            val intent = Intent(this, DiarioActivity::class.java)
            intent.putExtra("vid", videoUri.toString())
            startActivity(intent)
        }
    }

    private fun checkPermissions(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}