package com.example.todolist

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.coroutines.launch
import java.util.*

class SecondActivity : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private lateinit var labelText : TextView
    private lateinit var editText : EditText
    private lateinit var addButton: ImageButton
    private lateinit var dellButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_display)

        // Enables Always-on
        ambientController = AmbientModeSupport.attach(this)

        editText = findViewById(R.id.edit_info)
        labelText = findViewById(R.id.label_second)
        addButton = findViewById(R.id.add_action)
        dellButton = findViewById(R.id.remove_action)

        //val db = AppDatabase()

        val message = intent.getStringExtra(EXTRA_MESSAGE)
        readMessage(message.toString())
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun readMessage (mes : String) {
        if (mes[0] == 'A') {
            labelText.text = "Новый\nпункт"
            editText.setText("Введите текст")
            dellButton.setOnClickListener {
                this@SecondActivity.setResult(0)
                super.finish()
            }
            addButton.setOnClickListener{
                val textOnView = editText.text.toString()
                if (textOnView.isNotBlank() && textOnView != "Введите текст") {
                    scope.launch {
                        AppDateHelper.getInstance(applicationContext)!!.todoDao().insertAll(
                                TodoEntity(
                                        content = editText.text.toString(),
                                        create_on = Calendar.getInstance().time.time,
                                ))
                        runOnUiThread {
                            this@SecondActivity.setResult(-1)
                            super.finish()
                        }
                    }
                }
            }
        }
        else {
            val idText = mes.substring(1).toLong()
            var elem : TodoEntity
            labelText.text = "От\n${SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(idText))}"
            scope.launch {
                elem = AppDateHelper.getDAO(applicationContext).findById(idText)
                runOnUiThread {
                    editText.setText(elem.content)
                    dellButton.setOnClickListener {
                        scope.launch {
                            AppDateHelper.getDAO(applicationContext).delete(elem)
                            runOnUiThread {
                                this@SecondActivity.setResult(-1)
                                super.finish()
                            }
                        }
                    }
                    addButton.setOnClickListener {
                        val textOnView = editText.text.toString()
                        if (textOnView.isNotBlank()) {
                            if (elem.content != textOnView) {
                                elem.content = textOnView
                                scope.launch {
                                    AppDateHelper.getDAO(applicationContext).updateTodo(elem)
                                    runOnUiThread {
                                        this@SecondActivity.setResult(-1)
                                        super.finish()
                                    }
                                }
                            }
                            else {
                                this@SecondActivity.setResult(0)
                                super.finish()
                            }
                        }
                    }
                }
            }
        }
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