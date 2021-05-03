package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


const val EXTRA_MESSAGE = "com.example.todolist.MESSAGE1"

enum class ActionType(val bol: Boolean) {
    OPEN(true),
    ADD(false)
}

val scope = CoroutineScope(Dispatchers.Default + Job())

open class MainActivity : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private lateinit var recyclerView: WearableRecyclerView
    private lateinit var bar : ProgressBar
    private lateinit var menuItems : List<TodoEntity>
    private lateinit var toDoDao : TodoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)

        // Enables Always-on
        ambientController = AmbientModeSupport.attach(this)

        recyclerView = findViewById(R.id.recycler_launcher_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.isEdgeItemsCenteringEnabled = true
        recyclerView.layoutManager = WearableLinearLayoutManager(this)

        toDoDao = UserDb.getInstance(applicationContext)!!.todoDao()

        //enable wheel scrolling
        recyclerView.apply {
            isCircularScrollingGestureEnabled = true
            bezelFraction = 0.5f
            scrollDegreesPerScreen = 90f
        }

        bar = findViewById(R.id.main_progress)

        updateList()
    }

    fun sendMessage(msg: String) {
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, msg)
        }
        startActivityForResult(intent, 0)
    }

    private fun updateList() {
        bar.visibility = ProgressBar.VISIBLE
        recyclerView.visibility = View.INVISIBLE

        scope.launch {
            menuItems = toDoDao.getAllSorted()
            runOnUiThread {
                recyclerView.adapter = MainMenuAdapter(menuItems, object :
                        MainMenuAdapter.AdapterCallback {
                    override fun onItemClicked(menuPosition: Long) {
                        sendMessage(createMes(menuPosition))
                    }
                })
                bar.visibility = ProgressBar.INVISIBLE
                recyclerView.visibility = View.VISIBLE
                recyclerView.requestFocus()
            }
        }
    }

    fun createMes(code: Long): String {
        return if (code == -1L) "A"
        else "O$code"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode == -1)
            updateList()
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