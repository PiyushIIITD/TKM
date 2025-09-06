package com.xyz.tkm.viewmodel
import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
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
        allTasks = repository.getAllTasks()

        viewModelScope.launch {
            try{
            repository.syncFromFirestore()
        } catch (e: Exception){
        }
        }
    }

    fun addTask(task: Task) = viewModelScope.launch {
        repository.addTask(task)
        FirebaseHelper.syncTask(task)
    }

    fun clearAllTasks() = viewModelScope.launch {
        repository.clearAllTasks()
    }


    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
        FirebaseHelper.delete(task.Id)
    }
    fun syncRemoteToLocal() = viewModelScope.launch {
        try {
            repository.syncFromFirestore()
        } catch (e: Exception) { }
    }
}