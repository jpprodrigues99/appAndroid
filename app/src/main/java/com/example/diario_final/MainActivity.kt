package com.example.diario_final

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CloudMediaProviderContract.MediaCollectionInfo
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var our_request_code: Int = 123
    private lateinit var videoView: VideoView


    val RESQUEST_VIDEO_CAPTURE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //videoView = findViewById(R.id.videoViewAmigos)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationViewAmigos)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        val btnPrivado = findViewById<Button>(R.id.buttonPrivado)
        btnPrivado.setOnClickListener {
            val intent = Intent(this, PrivadoActivity::class.java)
            startActivity(intent)

        }

        initVideos()


    }

    private fun initVideos() {
        val layoutManager = LinearLayoutManager(this)

    }

    val navListener = BottomNavigationView.OnNavigationItemSelectedListener(){item->

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
                println(MediaStore.ACTION_VIDEO_CAPTURE)
                try {
                    startActivityForResult(take,RESQUEST_VIDEO_CAPTURE)

                }catch (e: ActivityNotFoundException){
                    Toast.makeText(this,"ERROR" + e.localizedMessage,Toast.LENGTH_SHORT).show()
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
            println(videoUri?.getPath())
            val intent = Intent(this,DiarioActivity::class.java)
            intent.putExtra("vid",videoUri.toString())
            startActivity(intent)
            //videoView.setVideoURI(videoUri)
            //videoView.start()

        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri = data?.data


            val intent = Intent(this, MainActivity::class.java).also {
                //intent.putExtra("vid",videoUri)
                //startActivity(intent)
                //videoView.setVideoURI(videoUri)
                //videoView.start()

            }
        }
    }*/



}