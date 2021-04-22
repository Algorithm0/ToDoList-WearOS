package com.example.todolist

import androidx.room.*

@Entity(tableName = "todo_items")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "create_on") var create_on: Long
)

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_items")
    fun getAll(): List<TodoEntity>

    @Query("SELECT * FROM todo_items WHERE id LIKE :id")
    fun findById(id: Int): TodoEntity

    @Insert
    fun insertAll(vararg todo: TodoEntity)

    @Delete
    fun delete(todo: TodoEntity)

    @Update
    fun updateTodo(vararg todos: TodoEntity)
}

@Database(entities = arrayOf(TodoEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}