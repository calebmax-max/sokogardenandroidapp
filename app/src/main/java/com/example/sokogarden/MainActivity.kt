package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        Find the buttons by the use of their ids
        val signupButton = findViewById<Button>(R.id.signupBtn)
        val signinButton = findViewById<Button>(R.id.signinBtn)
// Create the intents to the two activities
        signupButton.setOnClickListener {
            val intent = Intent(applicationContext, Signup::class.java)
            startActivity(intent)
        }

//        ===================================================
        signinButton.setOnClickListener {
            val intent = Intent(applicationContext, Signin::class.java)
            startActivity(intent)
        }
//        Find the recyclerView and the progressBar by their ids
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressbar)

//        specify  the API URL endpoint for fetching  the products(alwaysdat)
        val api = "https://kbenkamotho.alwaysdata.net/api/get_products"

//        Import the helper class
        val helper = ApiHelper(applicationContext)

//        inside of the helper class, access the function load prosucts
        helper.loadProducts(api, recyclerView, progressBar)
//        Find the about button by its id and have the intent
        val aboutButton = findViewById<Button>(R.id.aboutbtn)
//        Below is the intent to the about activity
        aboutButton.setOnClickListener {
            val intent = Intent(applicationContext, About::class.java)
            startActivity(intent)
        }
    }
}