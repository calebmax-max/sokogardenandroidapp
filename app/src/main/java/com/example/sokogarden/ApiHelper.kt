package com.example.sokogarden

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams

import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONArray
import org.json.JSONObject

class ApiHelper(var context: Context) {

    private fun paramsToJson(params: RequestParams?): JSONObject {
        val json = JSONObject()
        params?.let {
            val paramString = it.toString()
            val pairs = paramString.split("&")
            for (pair in pairs) {
                val parts = pair.split("=")
                if (parts.size == 2) {
                    json.put(parts[0], parts[1])
                }
            }
        }
        return json
    }

    fun post(api: String, params: RequestParams) {
        Toast.makeText(context, "Please wait for response", Toast.LENGTH_LONG).show()
        val client = AsyncHttpClient(true, 80, 443)
        val jsonParams = paramsToJson(params)
        val entity = StringEntity(jsonParams.toString())

        client.post(context, api, entity, "application/json", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                val message = response?.optString("message") ?: "Action successful"
                Toast.makeText(context, "Response: $message", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                val errorMsg = errorResponse?.optString("message") ?: "Error occurred"
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun post_login(api: String, params: RequestParams) {
        Toast.makeText(context, "Please wait for response", Toast.LENGTH_LONG).show()
        val client = AsyncHttpClient(true, 80, 443)
        val jsonParams = paramsToJson(params)
        val entity = StringEntity(jsonParams.toString())

        client.post(context, api, entity, "application/json", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                val message = response?.optString("message") ?: ""
                if (message.contains("successful", ignoreCase = true) || response?.optString("status") == "success") {
                    val user = response?.optJSONObject("user")
                    val username = user?.optString("username") ?: ""
                    val email = user?.optString("email") ?: ""

                    val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putString("username", username)
                    editor.putString("email", email)
                    editor.putBoolean("is_logged_in", true)
                    editor.apply()

                    Toast.makeText(context, "Welcome $username", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "$message", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                val errorMsg = errorResponse?.optString("message") ?: "Login failed"
                Toast.makeText(context, "Error: $errorMsg", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun loadProducts(url: String, recyclerView: RecyclerView, progressBar: ProgressBar? = null) {
        progressBar?.visibility = View.VISIBLE
        // Set layout manager if not already set
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
        val client = AsyncHttpClient(true, 80, 443)

        client.get(context, url, null, "application/json", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray) {
                progressBar?.visibility = View.GONE
                // UNCOMMENTED AND UPDATED:
                val productList = ProductAdapter.fromJsonArray(response)
                val adapter = ProductAdapter(productList)
                recyclerView.adapter = adapter
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                progressBar?.visibility = View.GONE
                Toast.makeText(context, "Failed to load products: $responseString", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun get(api: String, callBack: CallBack) {
        val client = AsyncHttpClient(true, 80, 443)
        client.get(context, api, null, "application/json", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONArray) { callBack.onSuccess(response) }
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) { callBack.onSuccess(response) }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) { callBack.onFailure(responseString) }
        })
    }

    interface CallBack {
        fun onSuccess(result: JSONArray?)
        fun onSuccess(result: JSONObject?)
        fun onFailure(result: String?)
    }
}