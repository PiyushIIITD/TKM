package com.xyz.tkm.network

import com.google.firebase.firestore.FirebaseFirestore
import com.xyz.tkm.model.Task

object FirebaseHelper {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tasks")

    fun syncTask(task: Task) {
        collection.document(task.Id.toString()).set(task)
    }

    fun delete(taskId: Int) {
        collection.document(taskId.toString()).delete()
    }

    fun fetchTasks(onComplete: (List<Task>) -> Unit) {
        collection.get().addOnSuccessListener { snapshot ->
            val tasks = snapshot.toObjects(Task::class.java)
            onComplete(tasks)
        }
    }
}
