package com.example.todolist

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.jupiter.api.*
import java.io.IOException
import org.junit.jupiter.api.Assumptions.assumeTrue


@DisplayName("Test database: write and write functions")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleEntityReadWriteTest {
    private lateinit var dao: TodoDao
    private lateinit var db: AppDatabase

    @BeforeAll
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = UserDb.getInstance(context, true)!!
        dao = db.todoDao()
    }

    @AfterAll
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private fun entityEq(one: TodoEntity, two : TodoEntity) : Boolean {
        return one.content == two.content && one.create_on == two.create_on
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val one = TodoEntity("test content string", 158)
        dao.insertAll(one)
        val two = dao.findById(158)
        assumeTrue(entityEq(one, two))
    }
}