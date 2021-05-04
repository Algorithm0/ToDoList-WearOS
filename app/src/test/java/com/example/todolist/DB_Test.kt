package com.example.todolist

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Assume.assumeTrue
import org.junit.Assume.assumeFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException

//use with JRE version 9 and younger
@Config(manifest=Config.NONE)
@RunWith(AndroidJUnit4::class)
class AppDataBaseTest {
    private lateinit var todoDao: TodoDao
    private val job = Job()
    private val scopeTest = CoroutineScope(Dispatchers.Main + job)

    @Before
    fun createDb() {
        todoDao = AppDateHelper.getDAO(ApplicationProvider.getApplicationContext(), true)
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
    fun writeElemAndRead() = runBlocking {
        val one = TodoEntity("test content string", 158)
        var two = TodoEntity("", 0)
        val job = GlobalScope.launch {
            todoDao.insertAll(one)
            two = todoDao.findById(158)
        }
        job.join()
        assumeTrue(entityEq(one, two))
    }

    @Test
    @Throws(Exception::class)
    fun removeNotExistElem() = runBlocking {
        val one = TodoEntity("test content string", 251)
        val job = GlobalScope.launch {
            todoDao.delete(one)
        }
        job.join()
    }

    @Test
    @Throws(Exception::class)
    fun writeElemAndRemove() = runBlocking {
        val one = TodoEntity("test content string", 365)
        val job = GlobalScope.launch {
            todoDao.insertAll(one)
            todoDao.delete(one)
        }
        job.join()
    }

    @Test
    fun aliveInstanceInAppDateHelper() {
        assumeFalse(AppDateHelper.instanceIsNull())
    }
}