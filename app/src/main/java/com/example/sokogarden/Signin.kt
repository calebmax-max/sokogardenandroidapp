package com.example.sokogarden

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.loopj.android.http.RequestParams

class Signin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        Find the two EditText, a button and a text view by usr of their ids
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val signinButton = findViewById<Button>(R.id.signinBtn)
        val signupTextView = findViewById<TextView>(R.id.signuptxt)

//        On the text view set on click listener such that when it is clicked it takes you to the sign up page
        signupTextView.setOnClickListener {
            val intent = Intent(applicationContext, Signup::class.java)
            startActivity(intent)
        }
//        On click of the button sign in , we need to interact with our api endpoint, as we pass the two data info, email and password
        signinButton.setOnClickListener {
//          Specify the API endpoint
            val api = "https://kbenkamotho.alwaysdata.net/api/signin"
//            create a RequestParams that will enable you to hold data in form of a bundle/package
            val data = RequestParams()

//            Add/append/ attach the email and the password
            data.put("email", email.text.toString())
            data.put("password", password.text.toString())

//            import the API helper
            val helper = ApiHelper(applicationContext)

//            By use of the function post_login  inside of the helper class , post your data
            helper.post_login(api, data)
        }
    }
}