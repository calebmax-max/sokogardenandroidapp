package com.example.sokogarden

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class About : AppCompatActivity() {

    // Declare variable that will hold the text to be spoken by the speech object
    lateinit var tts: TextToSpeech
    private var isTtsReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the UI elements
        val textview = findViewById<TextView>(R.id.aboutTxt)
        val speakButton = findViewById<Button>(R.id.btnListen)
        val genderSpinner = findViewById<Spinner>(R.id.genderSpinner)

        // Setup the Spinner
        val genders = arrayOf("Female", "Male")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter

        // Initialize TTS
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                isTtsReady = true
                
                // Set initial voice based on current spinner selection
                updateVoice(genderSpinner.selectedItem.toString())
            }
        }

        // Handle Spinner selection changes
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isTtsReady) {
                    updateVoice(genders[position])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        speakButton.setOnClickListener {
            if (isTtsReady) {
                tts.speak(textview.text, TextToSpeech.QUEUE_FLUSH, null, "AboutID")
            }
        }
    }

    private fun updateVoice(gender: String) {
        val voices = tts.voices
        if (voices != null) {
            for (voice in voices) {
                if (gender == "Female") {
                    // Look for common female voice identifiers
                    if (voice.name.contains("female", ignoreCase = true) || 
                        voice.name.contains("en-us-x-sfg", ignoreCase = true)) {
                        tts.voice = voice
                        tts.setPitch(1.2f) // Higher pitch for female
                        break
                    }
                } else {
                    // Look for common male voice identifiers
                    if (voice.name.contains("male", ignoreCase = true) || 
                        voice.name.contains("en-us-x-iol", ignoreCase = true)) {
                        tts.voice = voice
                        tts.setPitch(0.9f) // Lower pitch for male
                        break
                    }
                }
            }
        }
        tts.setSpeechRate(0.85f) // Clear, professional speed
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
