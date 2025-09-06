package com.xyz.tkm.repository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.SetOptions
import com.xyz.tkm.data.TaskDao
import com.xyz.tkm.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    private val db = FirebaseFirestore.getInstance()
    private val taskCollection = db.collection("tasks")

    fun getAllTasks() = taskDao.getAllTasks()

    suspend fun addTask(task: Task) {
        val Id = taskDao.insert(task)
        val addedTask = task.copy(Id = Id.toInt())

        val docRef = taskCollection.document()
        addedTask.firebaseId = docRef.id
        docRef.set(addedTask).await()

        addedTask.synced = true
        taskDao.update(addedTask)
    }

    suspend fun updateTask(task: Task) {
        taskDao.update(task)
        if (task.firebaseId.isNotEmpty()) {
            taskCollection.document(task.firebaseId).set(task, SetOptions.merge()).await()
        }
        else{
            val referal = taskCollection.document()
            task.firebaseId = referal.id
            task.synced = true
            referal.set(task).await()
            taskDao.update(task)
        }
    }

    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
        if (task.firebaseId.isNotEmpty()) {
            taskCollection.document(task.firebaseId).delete().await()
        }
    }

    suspend fun clearAllTasks() {
        taskDao.clearAllTasks()
    }

    suspend fun syncFromFirestore() {
        val snapshot = taskCollection.get().await()
        val tasks = snapshot.toObjects(Task::class.java)
        for (task in tasks) {
            task.synced = true
            val exists = if (task.firebaseId.isNotEmpty()) taskDao.getByFirebaseId(task.firebaseId) else null
            if (exists!=null){
                val updated = exists.copy(
                    title = task.title,
                    description = task.description,
                    status = task.status,
                    synced = true,
                    firebaseId = task.firebaseId
                )
                taskDao.update(updated)
            } else {
                taskDao.insert(task)
            }
        }
    }

}