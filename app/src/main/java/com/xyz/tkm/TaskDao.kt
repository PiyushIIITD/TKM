package com.xyz.tkm.data
import androidx.lifecycle.LiveData
import androidx.room.*
import com.xyz.tkm.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY Id DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks")
    suspend fun clearAllTasks()


    @Query("SELECT * FROM tasks WHERE firebaseId = :fid LIMIT 1")
    suspend fun getByFirebaseId(fid: String): Task?
}
