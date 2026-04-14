package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loopj.android.http.RequestParams

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Apply Animation to CardView
        val signupCard = findViewById<CardView>(R.id.signupCard)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        signupCard.startAnimation(slideUp)

//        Find all views by use of their ids
        val username = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val phone = findViewById<EditText>(R.id.phone)
        val signupButton = findViewById<Button>(R.id.signupBtn)
        val signinTextView = findViewById<TextView>(R.id.signintxt)

//        Below when a person clicks n the textview , he or she is navigated to the signin page
        signinTextView.setOnClickListener {
            val intent = Intent(applicationContext, Signin::class.java)
            startActivity(intent)
        }
//        On click of the sign uo button we want to register a person
        signupButton.setOnClickListener {
//            Specify the API endpoint
            val api = "http://calebtonny.alwaysdata.net/api/signup"
//            create a RequestParams that will enable you to hold data in form of a bundle/package
            val data = RequestParams()
//            Add/append/ attach the username, email, password and phone number
            data.put("username", username.text.toString().trim())
            data.put("email", email.text.toString().trim())
            data.put("password", password.text.toString().trim())
            data.put("phone", phone.text.toString().trim())

            email.text.clear()
            password.text.clear()
            phone.text.clear()
            username.text.clear()

//            import the API helper
            val helper = ApiHelper(applicationContext)
//            By use of the function post  inside of the helper class , post your data
            helper.post(api, data)
        }
    }
}