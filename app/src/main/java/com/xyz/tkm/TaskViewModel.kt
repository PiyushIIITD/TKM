package com.xyz.tkm.viewmodel
import android.app.Application
import androidx.lifecycle.*
import com.xyz.tkm.data.TaskDatabase
import com.xyz.tkm.model.Task
import com.xyz.tkm.network.FirebaseHelper
import com.xyz.tkm.repository.TaskRepository
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore


class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val allTasks: LiveData<List<Task>>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
    }

    fun insert(task: Task) = viewModelScope.launch {
        repository.insert(task)
        FirebaseHelper.syncTask(task)
    }

    fun update(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.delete(task)
        FirebaseHelper.deleteTask(task.id)

    }
}
