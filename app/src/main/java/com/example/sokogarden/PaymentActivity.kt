package com.example.sokogarden

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.loopj.android.http.RequestParams

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        Find the views by use of their ids
        val txtname = findViewById<TextView>(R.id.txtProductName)
        val imgProduct = findViewById<ImageView>(R.id.imgProduct)
        val txtcost = findViewById<TextView>(R.id.txtProductCost)
        val txtDescription = findViewById<TextView>(R.id.txtProductDescription)


//        Retrive the  data passed from the previous activity
        val name = intent.getStringExtra("product_name")
        val cost = intent.getIntExtra("product_cost", 0)
        val description = intent.getStringExtra("product_description")
        val product_photo = intent.getStringExtra("product_photo")

//        update the textviews with the data passed from the previous activity
        txtname.text = name
        txtcost.text = "Kes $cost"
        txtDescription.text = description

//        Specify the image url - Fixed the URL syntax
        val imageUrl = "https://kbenkamotho.alwaysdata.net/static/images/$product_photo"

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.mipmap.ic_launcher) // Corrected resource reference
            .into(imgProduct)

//        Find the edit text and the pay now button by use of their ids
        val phone = findViewById<EditText>(R.id.phone)
        val btnpay = findViewById<Button>(R.id.pay)

//        set click listener
        btnpay.setOnClickListener {
//            Specify the api endpoint for the payment
            val api = "https://kbenkamotho.alwaysdata.net/api/mpesa_payment                                        "
//             create a request params
            val data = RequestParams()

//            Insert data into the request params
            data.put("amount", cost)
            data.put("phone", phone.text.toString().trim())

//            Import the helper class
            val helper = ApiHelper(applicationContext)
//            Access the post function inside of the helper class
            helper.post(api, data)
//            clear the phone number from the edit text
            phone.text.clear()
        }
    }
}