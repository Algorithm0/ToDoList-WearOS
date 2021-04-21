package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val EXTRA_MESSAGE = "com.example.todolist.MESSAGE"

enum class ActionType(val bol: Boolean) {
    OPEN (true),
    ADD (false)
}

val job: Job = Job()
val scope = CoroutineScope(Dispatchers.Default + job)

class MainActivity : AppCompatActivity(), AmbientModeSupport.AmbientCallbackProvider {

    private lateinit var recyclerView: WearableRecyclerView
    private lateinit var bar : ProgressBar
    private lateinit var menuItems : List<OneToDo>
    private val oneToDoDAO = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "database"
    ).build().OneToDoDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        ambientController = AmbientModeSupport.attach(this)

        recyclerView = findViewById(R.id.recycler_launcher_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.isEdgeItemsCenteringEnabled = true
        recyclerView.layoutManager = WearableLinearLayoutManager(this)

        recyclerView.apply {
            isCircularScrollingGestureEnabled = true
            bezelFraction = 0.5f
            scrollDegreesPerScreen = 90f
        }

        bar = findViewById(R.id.main_progress)

        updateList()
    }

    fun sendMessage(msg : String) {
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, msg)
        }
        startActivity(intent)
    }

    private fun updateList() {
        bar.visibility = ProgressBar.VISIBLE
        recyclerView.visibility = View.INVISIBLE
//
        scope.launch {
            menuItems = oneToDoDAO.getAll()
            runOnUiThread {
                recyclerView.adapter = MainMenuAdapter(menuItems, object :
                    MainMenuAdapter.AdapterCallback {
                    override fun onItemClicked(menuPosition: Int?) {
                        when (menuPosition) {
                            null -> {}
                            else -> sendMessage(createMes(menuPosition))
                        }
                    }
                })
                bar.visibility = ProgressBar.INVISIBLE
                recyclerView.visibility = View.VISIBLE
                recyclerView.requestFocus()
            }
        }
    }

    fun createMes(code: Int): String {
        return if (code == -222) "A"
        else "O${oneToDoDAO.loadByIds(code).text}"
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
