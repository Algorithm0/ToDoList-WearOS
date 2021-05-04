package com.example.todolist

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assume.assumeTrue
import org.junit.Assume.assumeFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

//use with JRE version 9
@RunWith(AndroidJUnit4::class)
class AppDataBaseTest {
    private lateinit var todoDao: TodoDao
    private val scopeTest = CoroutineScope(Dispatchers.Main + Job())

    @Before
    fun createDb() {
        todoDao = AppDateHelper.getDAO(ApplicationProvider.getApplicationContext<Context>(), true)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        AppDateHelper.destroyInstance()
    }

    private fun entityEq(one: TodoEntity, two : TodoEntity) : Boolean {
        return one.content == two.content && one.create_on == two.create_on
    }

    @Test
    @Throws(Exception::class)
    fun writeElemAndReadInList() {
        val one = TodoEntity("test content string", 158)
        scopeTest.launch {
            todoDao.insertAll(one)
            val two = todoDao.findById(158)
            assumeTrue(entityEq(one, two))
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeNotExistElemInList() {
        val one = TodoEntity("test content string", 251)
        scopeTest.launch {
            todoDao.delete(one)
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeElemInList() {
        val one = TodoEntity("test content string", 365)
        scopeTest.launch {
            todoDao.delete(one)
        }
    }

    @Test
    fun aliveInstanceInAppDateHelper() {
        assumeFalse(AppDateHelper.instanceIsNull())
    }
}