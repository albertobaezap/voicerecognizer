package com.example.alberto.voicerecognizer

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.alberto.voicerecognizer.R.id.display_text
import com.example.alberto.voicerecognizer.R.id.text
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var speechRecognizer: SpeechRecognizer
    lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(SpeechRecognizerListener(display_text))

        speak_button.setOnClickListener {
            promptSpeechInput()
        }

        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
            if(it != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.getDefault()
            }
        })

        play_button.setOnClickListener {
            val toSpeak = display_text.text.toString()
            textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "New request")
        }


    }

    fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.example.alberto.voicerecognizer")
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
        speechRecognizer.startListening(intent)
    }

    class SpeechRecognizerListener(private val display_text: TextView) : RecognitionListener {

        override fun onReadyForSpeech(params: Bundle?) {
            Log.d(TAG, "onReadyForSpeech")
        }

        override fun onRmsChanged(rmsdB: Float) {
            Log.d(TAG, "onRmsChanged $rmsdB")
        }

        override fun onBufferReceived(buffer: ByteArray?) {
            Log.d(TAG, "onBufferReceived $buffer")
        }

        override fun onPartialResults(partialResults: Bundle?) {
            Log.d(TAG, "onPartialResults")
        }

        override fun onEvent(eventType: Int, params: Bundle?) {
            Log.d(TAG, "onEvent $eventType")
        }

        override fun onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech")
        }

        override fun onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech")
        }

        override fun onError(error: Int) {
            Log.d(TAG, "onError $error")
        }

        override fun onResults(results: Bundle?) {
            var str = String()
            Log.d(TAG, "onResults $results")
            results?.let {
                val data = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                for (i in 0 until data.size) {
                    Log.d(TAG, "result " + data[i])
                    str += data[i]
                }
                Log.v(TAG, "results: " + data.size)
                display_text.text = data[0]
            }
        }
    }
}

