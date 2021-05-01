package com.example.todolist

import android.content.Context
import androidx.room.*

@Entity(tableName = "todo_items")
data class TodoEntity(
    @ColumnInfo(name = "content") var content: String,
    @PrimaryKey var create_on: Long
)

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_items")
    fun getAll(): List<TodoEntity>

    @Query("SELECT * FROM todo_items WHERE create_on LIKE :id")
    fun findById(id: Long): TodoEntity

    @Insert
    fun insertAll(vararg todo: TodoEntity)

    @Delete
    fun delete(todo: TodoEntity)

    @Update
    fun updateTodo(vararg todos: TodoEntity)
}

@Database(entities = [TodoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

abstract class UserDb : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "todo-list.db").build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}