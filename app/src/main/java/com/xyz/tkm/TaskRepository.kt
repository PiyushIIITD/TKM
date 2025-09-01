package com.xyz.tkm.repository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.LiveData
import com.xyz.tkm.data.TaskDao
import com.xyz.tkm.model.Task

class TaskRepository(private val taskDao: TaskDao) {
    private val db = FirebaseFirestore.getInstance()
    private val taskCollection = db.collection("tasks")
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    
    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }
}
