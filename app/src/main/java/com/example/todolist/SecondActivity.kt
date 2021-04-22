package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport

class SecondActivity : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_display)

        val message = intent.getStringExtra(EXTRA_MESSAGE)

        readMessage(message.toString())
    }

    private fun readMessage (mes : String) {

    }

    // Enables Always-on
    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback = MyAmbientCallback()
    private class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {

        override fun onEnterAmbient(ambientDetails: Bundle?) {
            // Handle entering ambient mode
        }

        override fun onExitAmbient() {
            // Handle exiting ambient mode
        }

        override fun onUpdateAmbient() {
            // Update the content
        }
    }
    private lateinit var ambientController: AmbientModeSupport.AmbientController
}