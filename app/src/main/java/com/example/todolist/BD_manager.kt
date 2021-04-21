package com.example.todolist

import androidx.room.*
import java.sql.Timestamp

@Entity
data class OneToDo(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "text") val text: String?,
    @ColumnInfo(name = "create_on") val createOn: Long
)

@Dao
interface OneToDoDAO {
    @Query("SELECT * FROM OneToDo")
    fun getAll(): List<OneToDo>

    @Query("SELECT * FROM OneToDo WHERE id IN (:id)")
    fun loadByIds(id: Int): OneToDo

    @Insert
    fun insertAll(vararg users: OneToDo)

    @Delete
    fun delete(user: OneToDo)
}

//@Database(entities = [OneToDo::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun OneToDoDAO(): OneToDoDAO
//}