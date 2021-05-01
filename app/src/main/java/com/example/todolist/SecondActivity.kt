package com.example.todolist

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.wear.ambient.AmbientModeSupport
import kotlinx.coroutines.launch
import com.example.todolist.AppDatabase as AppDatabase

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

    @SuppressLint("SetTextI18n")
    private fun readMessage (mes : String) {
        if (mes[0] == 'A') {
            labelText.text = "Новый\nпункт"
            editText.setText("Введите текст")
            dellButton.setOnClickListener {
                super.finish()
            }
            addButton.setOnClickListener{
                scope.launch {
                    UserDb.getInstance(applicationContext)!!.todoDao().insertAll(
                            TodoEntity(
                                    content = editText.text.toString(),
                                    create_on = Calendar.getInstance().time.time,
                            ))
                    runOnUiThread {
                        super.finishActivity(0)
                    }
                }

            }
        }

        //else "O${toDoDao.findById(code).content}"
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